package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.enums.FeeFrequency;
import com.portal.studymate.accounts.enums.StudentFeeStatus;
import com.portal.studymate.accounts.model.FeePlan;
import com.portal.studymate.accounts.model.FeePlanItem;
import com.portal.studymate.accounts.model.FeeStructure;
import com.portal.studymate.accounts.model.StudentFee;
import com.portal.studymate.accounts.repository.FeePlanRepository;
import com.portal.studymate.accounts.repository.FeeStructureRepository;
import com.portal.studymate.accounts.repository.StudentFeeRepository;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.repository.SchoolRepository;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentEnrollment;
import com.portal.studymate.student.repository.StudentEnrollmentRepository;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeeSchedulerService {

    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final FeePlanRepository feePlanRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final StudentFeeRepository studentFeeRepository;
    private final StudentEnrollmentRepository enrollmentRepository;
    private final FeeDiscountService feeDiscountService;

    /**
     * Monthly fee generation — runs 1st of each month at 1 AM.
     * For each school, finds active students with fee plans,
     * and generates MONTHLY fee records for the current month.
     */
    @Scheduled(cron = "0 0 1 1 * *")
    @Transactional
    public void generateMonthlyFeesForAllSchools() {
        log.info("Monthly fee generation job started");
        List<School> schools = schoolRepository.findAll();
        for (School school : schools) {
            try {
                int generated = generateMonthlyFeesForSchool(school);
                log.info("School {}: generated {} monthly fee records", school.getName(), generated);
            } catch (Exception e) {
                log.error("Failed to generate fees for school {}: {}", school.getId(), e.getMessage());
            }
        }
        log.info("Monthly fee generation job completed");
    }

    private int generateMonthlyFeesForSchool(School school) {
        List<Student> students = studentRepository.findBySchoolAndStatusNot(
            school, com.portal.studymate.student.model.StudentStatus.TRANSFERRED);
        int count = 0;

        for (Student student : students) {
            if (student.getFeePlanId() == null) continue;

            var planOpt = feePlanRepository.findById(student.getFeePlanId());
            if (planOpt.isEmpty() || !planOpt.get().isActive()) continue;

            FeePlan plan = planOpt.get();
            if (plan.getItems() == null) continue;

            var enrollmentOpt = enrollmentRepository.findActiveByStudent(student);
            if (enrollmentOpt.isEmpty()) continue;

            StudentEnrollment enrollment = enrollmentOpt.get();

            for (FeePlanItem item : plan.getItems()) {
                if (item.getFrequency() != FeeFrequency.MONTHLY) continue;
                try {
                    boolean created = generateFeeForStudent(student, enrollment, item, school);
                    if (created) count++;
                } catch (Exception e) {
                    log.warn("Failed to generate fee for student {} type {}: {}",
                        student.getId(), item.getFeeType(), e.getMessage());
                }
            }
        }
        return count;
    }

    /**
     * Generate initial fees when a fee plan is assigned to a student.
     * Creates ONE_TIME fees immediately and first MONTHLY fee.
     */
    @Transactional
    public int generateInitialFeesForStudent(Long studentId, Long feePlanId) {
        log.info("Generating initial fees for student {} with plan {}", studentId, feePlanId);

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
        FeePlan plan = feePlanRepository.findById(feePlanId)
            .orElseThrow(() -> new RuntimeException("Fee plan not found: " + feePlanId));

        var enrollmentOpt = enrollmentRepository.findActiveByStudent(student);
        if (enrollmentOpt.isEmpty()) {
            log.warn("No active enrollment for student {}, skipping fee generation", studentId);
            return 0;
        }

        StudentEnrollment enrollment = enrollmentOpt.get();
        School school = student.getSchool();
        int count = 0;

        if (plan.getItems() == null) return 0;

        for (FeePlanItem item : plan.getItems()) {
            try {
                boolean created = generateFeeForStudent(student, enrollment, item, school);
                if (created) count++;
            } catch (Exception e) {
                log.warn("Failed to generate initial fee for student {} type {}: {}",
                    studentId, item.getFeeType(), e.getMessage());
            }
        }

        log.info("Generated {} initial fee records for student {}", count, studentId);
        return count;
    }

    private boolean generateFeeForStudent(Student student, StudentEnrollment enrollment,
                                          FeePlanItem item, School school) {
        // Find or create FeeStructure for this school/year/class/feeType
        var structureOpt = feeStructureRepository
            .findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
                school, enrollment.getAcademicYear(),
                enrollment.getSchoolClass(), item.getFeeType());

        FeeStructure structure;
        if (structureOpt.isPresent()) {
            structure = structureOpt.get();
        } else {
            // Auto-create fee structure from plan item
            structure = FeeStructure.builder()
                .school(school)
                .academicYear(enrollment.getAcademicYear())
                .schoolClass(enrollment.getSchoolClass())
                .feeType(item.getFeeType())
                .amount(item.getAmount())
                .dueDate(calculateDueDate(item.getFrequency()))
                .active(true)
                .build();
            structure = feeStructureRepository.save(structure);
            log.info("Auto-created fee structure: {} for class {}",
                item.getFeeType(), enrollment.getSchoolClass().getName());
        }

        // Check if StudentFee already exists
        var existingFee = studentFeeRepository.findByStudentAndFeeStructure(student, structure);
        if (existingFee.isPresent()) {
            return false; // Already exists
        }

        // Apply discounts
        BigDecimal finalAmount = feeDiscountService.calculateDiscountedAmount(
            item.getAmount(), student.getId(),
            enrollment.getAcademicYear().getId());

        // Create StudentFee record
        StudentFee fee = StudentFee.builder()
            .student(student)
            .feeStructure(structure)
            .totalAmount(finalAmount)
            .paidAmount(BigDecimal.ZERO)
            .status(StudentFeeStatus.PENDING)
            .dueDate(calculateDueDate(item.getFrequency()))
            .build();

        studentFeeRepository.save(fee);
        return true;
    }

    private LocalDate calculateDueDate(FeeFrequency frequency) {
        LocalDate now = LocalDate.now();
        return switch (frequency) {
            case ONE_TIME -> now.plusDays(15);
            case MONTHLY -> now.withDayOfMonth(Math.min(10, now.lengthOfMonth()));
            case QUARTERLY -> now.plusMonths(3).withDayOfMonth(10);
            case HALF_YEARLY -> now.plusMonths(6).withDayOfMonth(10);
            case ANNUAL -> now.plusMonths(12).withDayOfMonth(10);
        };
    }
}
