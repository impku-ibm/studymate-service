package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.accounts.dtos.responses.StudentFeeResponse;
import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.model.FeeStructure;
import com.portal.studymate.accounts.model.StudentFee;
import com.portal.studymate.accounts.model.StudentFeeConfiguration;
import com.portal.studymate.accounts.repository.FeeStructureRepository;
import com.portal.studymate.accounts.repository.StudentFeeConfigurationRepository;
import com.portal.studymate.accounts.repository.StudentFeeRepository;
import com.portal.studymate.accounts.service.AuditService;
import com.portal.studymate.accounts.service.StudentFeeService;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentEnrollment;
import com.portal.studymate.student.repository.StudentEnrollmentRepository;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UPDATED: Now uses StudentFeeConfiguration to generate fees.
 * Only generates fees for fee types that are configured for each student.
 * This allows flexible fee assignment per student.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentFeeServiceImpl implements StudentFeeService {

   private final StudentFeeRepository studentFeeRepository;
   private final FeeStructureRepository feeStructureRepository;
   private final StudentFeeConfigurationRepository studentFeeConfigurationRepository; // NEW: Added
   private final StudentRepository studentRepository;
   private final StudentEnrollmentRepository studentEnrollmentRepository;
   private final AcademicYearRepository academicYearRepository;
   private final SchoolClassRepository schoolClassRepository;
   private final AuditService auditService;

   @Override
   @Transactional(readOnly = true)
   public Page<StudentFeeResponse> getStudentFees(Long studentId, Pageable pageable) {
      Student student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

      return studentFeeRepository.findByStudent(student, pageable)
         .map(StudentFeeResponse::from);
   }

   /**
    * UPDATED: Generate fees based on student's fee configuration.
    * 
    * OLD BEHAVIOR: Generated ALL fee structures for the class
    * NEW BEHAVIOR: Only generates fees for fee types configured for this student
    * 
    * Example:
    * - Class 10 has fee structures: TUITION, TRANSPORT, HOSTEL, EXAM
    * - Student A configured: TUITION, TRANSPORT
    * - Student B configured: TUITION, HOSTEL, EXAM
    * Result: Each student gets only their configured fees
    */
   @Override
   public void generateFeesForEnrollment(Long enrollmentId) {
      StudentEnrollment enrollment = studentEnrollmentRepository.findById(enrollmentId)
         .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

      School school = SchoolContext.getSchool();
      
      // CHANGED: Get student's configured fee types instead of all class fee structures
      List<StudentFeeConfiguration> feeConfigs = studentFeeConfigurationRepository
         .findByStudentAndAcademicYearAndActiveTrue(
            enrollment.getStudent(), 
            enrollment.getAcademicYear()
         );

      // If no fee configuration exists, log warning and skip
      if (feeConfigs.isEmpty()) {
         log.warn("No fee configuration found for student {}. Skipping fee generation.", 
            enrollment.getStudent().getId());
         return;
      }

      // Generate fees only for configured fee types
      int generatedCount = 0;
      for (StudentFeeConfiguration config : feeConfigs) {
         // Find the fee structure for this fee type
         FeeStructure feeStructure = feeStructureRepository
            .findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
               school, 
               enrollment.getAcademicYear(), 
               enrollment.getSchoolClass(),
               config.getFeeType()
            ).orElse(null);

         // Skip if fee structure doesn't exist for this fee type
         if (feeStructure == null) {
            log.warn("Fee structure not found for fee type {} in class {}. Skipping.",
               config.getFeeType(), enrollment.getSchoolClass().getName());
            continue;
         }

         // Create student fee
         StudentFee studentFee = new StudentFee();
         studentFee.setStudent(enrollment.getStudent());
         studentFee.setFeeStructure(feeStructure);
         studentFee.setTotalAmount(feeStructure.getAmount());
         studentFee.setDueDate(feeStructure.getDueDate());

         studentFeeRepository.save(studentFee);
         generatedCount++;
         
         auditService.logAction(AuditAction.FEE_GENERATED, "StudentFee", studentFee.getId(),
            "Generated " + config.getFeeType() + " fee for student: " + enrollment.getStudent().getFullName());
      }

      log.info("Generated {} fees (out of {} configured) for enrollment {}", 
         generatedCount, feeConfigs.size(), enrollmentId);
   }

   @Override
   public void generateExamFeesForAllStudents(Long examId) {
      var school = SchoolContext.getSchool();
      
      // Get exam details (assuming you have exam repository access)
      // For now, we'll get all active students and generate exam fees
      var activeStudents = studentRepository.findBySchool(school);
      
      // Find exam fee structure for the school
      var examFeeStructures = feeStructureRepository.findBySchoolAndFeeType(school, FeeType.EXAM);
      
      if (examFeeStructures.isEmpty()) {
         log.warn("No exam fee structure found for school {}. Cannot generate exam fees.", school.getId());
         return;
      }
      
      int generatedCount = 0;
      for (var student : activeStudents) {
         // Check if student has exam fee configuration
         var hasExamFeeConfig = studentFeeConfigurationRepository
            .existsByStudentAndFeeTypeAndActiveTrue(student, FeeType.EXAM);
         
         if (!hasExamFeeConfig) {
            continue; // Skip if student doesn't have exam fee configured
         }
         
         // Find appropriate fee structure for student's class
         var studentEnrollment = studentEnrollmentRepository
            .findActiveByStudent(student)
            .orElse(null);
         
         if (studentEnrollment == null) {
            continue;
         }
         
         var examFeeStructure = examFeeStructures.stream()
            .filter(fs -> fs.getSchoolClass().getId().equals(studentEnrollment.getSchoolClass().getId()))
            .findFirst()
            .orElse(null);
         
         if (examFeeStructure == null) {
            continue;
         }
         
         // Check if exam fee already exists for this student
         var existingFee = studentFeeRepository
            .findByStudentAndFeeStructure(student, examFeeStructure)
            .orElse(null);
         
         if (existingFee != null) {
            continue; // Skip if fee already exists
         }
         
         // Create exam fee
         var studentFee = new StudentFee();
         studentFee.setStudent(student);
         studentFee.setFeeStructure(examFeeStructure);
         studentFee.setTotalAmount(examFeeStructure.getAmount());
         studentFee.setDueDate(examFeeStructure.getDueDate());
         
         studentFeeRepository.save(studentFee);
         generatedCount++;
         
         auditService.logAction(AuditAction.FEE_GENERATED, "StudentFee", studentFee.getId(),
            "Generated exam fee for student: " + student.getFullName());
      }
      
      log.info("Generated exam fees for {} students", generatedCount);
   }

   @Override
   public void generateAdmissionFeeForStudent(Long studentId) {
      var school = SchoolContext.getSchool();
      
      var student = studentRepository.findById(studentId)
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
      
      // Check if student has admission fee configuration
      var hasAdmissionFeeConfig = studentFeeConfigurationRepository
         .existsByStudentAndFeeTypeAndActiveTrue(student, FeeType.ADMISSION);
      
      if (!hasAdmissionFeeConfig) {
         log.warn("Student {} does not have admission fee configured", studentId);
         return;
      }
      
      // Get student's current enrollment
      var enrollment = studentEnrollmentRepository.findActiveByStudent(student)
         .orElseThrow(() -> new ResourceNotFoundException("Active enrollment not found for student"));
      
      // Find admission fee structure
      var admissionFeeStructure = feeStructureRepository
         .findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
            school, enrollment.getAcademicYear(), enrollment.getSchoolClass(), FeeType.ADMISSION)
         .orElse(null);
      
      if (admissionFeeStructure == null) {
         log.warn("Admission fee structure not found for class {}", enrollment.getSchoolClass().getName());
         return;
      }
      
      // Check if admission fee already exists
      var existingFee = studentFeeRepository
         .findByStudentAndFeeStructure(student, admissionFeeStructure)
         .orElse(null);
      
      if (existingFee != null) {
         log.info("Admission fee already exists for student {}", studentId);
         return;
      }
      
      // Create admission fee
      var studentFee = new StudentFee();
      studentFee.setStudent(student);
      studentFee.setFeeStructure(admissionFeeStructure);
      studentFee.setTotalAmount(admissionFeeStructure.getAmount());
      studentFee.setDueDate(admissionFeeStructure.getDueDate());
      
      studentFeeRepository.save(studentFee);
      
      auditService.logAction(AuditAction.FEE_GENERATED, "StudentFee", studentFee.getId(),
         "Generated admission fee for student: " + student.getFullName());
      
      log.info("Generated admission fee for student {}", studentId);
   }

   @Override
   @Transactional(readOnly = true)
   public Page<StudentFeeResponse> getStudentFeesByClass(Long classId, Long academicYearId, Pageable pageable) {
      // TODO: implement class-level student fee query
      return Page.empty(pageable);
   }

   @Override
   public void generateFeesForClass(Long academicYearId, Long classId) {
      School school = SchoolContext.getSchool();
      
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
      
      SchoolClass schoolClass = schoolClassRepository.findById(classId)
         .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

      List<StudentEnrollment> enrollments = studentEnrollmentRepository
         .findByAcademicYearAndSchoolClass(academicYear, schoolClass);

      for (StudentEnrollment enrollment : enrollments) {
         generateFeesForEnrollment(enrollment.getId());
      }

      log.info("Generated fees for {} students in class {}", enrollments.size(), schoolClass.getName());
   }
}