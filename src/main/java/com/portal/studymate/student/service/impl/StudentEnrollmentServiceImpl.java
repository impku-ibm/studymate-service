package com.portal.studymate.student.service.impl;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.student.dto.EnrollStudentRequest;
import com.portal.studymate.student.dto.StudentEnrollmentResponse;
import com.portal.studymate.student.model.EnrollmentStatus;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentEnrollment;
import com.portal.studymate.student.repository.StudentEnrollmentRepository;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.student.service.StudentEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

   private final StudentEnrollmentRepository enrollmentRepo;
   private final StudentRepository studentRepo;
   private final AcademicYearRepository academicYearRepo;
   private final SchoolClassRepository classRepo;
   private final ClassSectionTemplateRepository sectionRepo;
   private final SchoolContext schoolContext;

   @Override
   public StudentEnrollmentResponse enroll(EnrollStudentRequest req) {

      AcademicYear year = academicYearRepo
                             .findActiveBySchool(schoolContext.getSchool());

      Student student = studentRepo.findById(req.getStudentId())
                                   .orElseThrow();

      StudentEnrollment enrollment = StudentEnrollment.builder()
                                                      .student(student)
                                                      .academicYear(year)
                                                      .schoolClass(classRepo.findById(req.getClassId()).orElseThrow())
                                                      .section(sectionRepo.findById(req.getSectionId()).orElseThrow())
                                                      .rollNumber(req.getRollNumber())
                                                      .status(EnrollmentStatus.ACTIVE)
                                                      .build();

      return map(enrollmentRepo.save(enrollment));
   }

   @Override
   public List<StudentEnrollmentResponse> listActiveYear() {
      AcademicYear year = academicYearRepo
                             .findActiveBySchool(schoolContext.getSchool());

      return enrollmentRepo.findByAcademicYear(year)
                           .stream().map(this::map).toList();
   }

   private StudentEnrollmentResponse map(StudentEnrollment e) {
      return StudentEnrollmentResponse.builder()
                                      .rollNumber(e.getRollNumber())
                                      .studentName(e.getStudent().getFullName())
                                      .admissionNumber(e.getStudent().getAdmissionNumber())
                                      .className(e.getSchoolClass().getName())
                                      .sectionName(e.getSection().getName())
                                      .status(e.getStatus())
                                      .build();
   }
}

