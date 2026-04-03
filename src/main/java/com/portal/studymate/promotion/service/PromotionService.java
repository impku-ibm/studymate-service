package com.portal.studymate.promotion.service;

import com.portal.studymate.accounts.service.FeeSchedulerService;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.promotion.dto.BulkPromotionRequest;
import com.portal.studymate.promotion.dto.PromotionResultResponse;
import com.portal.studymate.student.dto.EnrollStudentRequest;
import com.portal.studymate.student.service.StudentEnrollmentService;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PromotionService {

    private final StudentEnrollmentService enrollmentService;
    private final StudentRepository studentRepository;
    private final FeeSchedulerService feeSchedulerService;

    public PromotionResultResponse bulkPromote(BulkPromotionRequest request) {
        int promoted = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        int rollNumber = 1;
        for (Long studentId : request.studentIds()) {
            try {
                var student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

                EnrollStudentRequest enrollReq = EnrollStudentRequest.builder()
                    .studentId(studentId)
                    .classId(request.targetClassId())
                    .sectionId(null)
                    .rollNumber(rollNumber)
                    .build();

                enrollmentService.enroll(enrollReq);

                // Preserve fee plan — regenerate fees for new class after promotion
                if (student.getFeePlanId() != null) {
                    try {
                        int generated = feeSchedulerService.generateInitialFeesForStudent(
                            studentId, student.getFeePlanId());
                        log.info("Generated {} fee records for promoted student {}", generated, studentId);
                    } catch (Exception ex) {
                        log.warn("Fee generation after promotion failed for student {}: {}", studentId, ex.getMessage());
                    }
                }

                promoted++;
                rollNumber++;
                log.info("Promoted student {} to class {}", student.getFullName(), request.targetClassId());
            } catch (Exception e) {
                skipped++;
                errors.add("Student " + studentId + ": " + e.getMessage());
                log.warn("Failed to promote student {}: {}", studentId, e.getMessage());
            }
        }

        log.info("Bulk promotion complete: {} promoted, {} skipped", promoted, skipped);
        return new PromotionResultResponse(promoted, skipped, errors);
    }
}
