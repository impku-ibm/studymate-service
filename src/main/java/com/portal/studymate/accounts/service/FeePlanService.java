package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateFeePlanRequest;
import com.portal.studymate.accounts.dtos.responses.FeePlanResponse;
import com.portal.studymate.accounts.model.FeePlan;
import com.portal.studymate.accounts.model.FeePlanItem;
import com.portal.studymate.accounts.repository.FeePlanRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeePlanService {

    private final FeePlanRepository feePlanRepository;
    private final StudentRepository studentRepository;

    public FeePlanResponse createFeePlan(CreateFeePlanRequest request) {
        log.info("createFeePlan called - name: {}", request.name());
        School school = SchoolContext.getSchool();

        FeePlan plan = FeePlan.builder()
            .school(school)
            .name(request.name())
            .description(request.description())
            .active(true)
            .build();

        List<FeePlanItem> items = request.items().stream()
            .map(i -> FeePlanItem.builder()
                .feePlan(plan)
                .feeType(i.feeType())
                .amount(i.amount())
                .frequency(i.frequency())
                .build())
            .toList();

        plan.setItems(items);
        FeePlan saved = feePlanRepository.save(plan);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FeePlanResponse> getFeePlans() {
        log.info("getFeePlans called");
        School school = SchoolContext.getSchool();
        return feePlanRepository.findBySchool(school).stream()
            .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FeePlanResponse getFeePlan(Long id) {
        log.info("getFeePlan called - id: {}", id);
        return toResponse(feePlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fee plan not found")));
    }

    public void deleteFeePlan(Long id) {
        log.info("deleteFeePlan called - id: {}", id);
        FeePlan plan = feePlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fee plan not found"));
        feePlanRepository.delete(plan);
    }

    public void assignPlanToStudent(Long studentId, Long feePlanId) {
        log.info("assignPlanToStudent called - studentId: {}, feePlanId: {}", studentId, feePlanId);
        School school = SchoolContext.getSchool();
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        feePlanRepository.findById(feePlanId)
            .orElseThrow(() -> new ResourceNotFoundException("Fee plan not found"));
        student.setFeePlanId(feePlanId);
        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public FeePlanResponse getStudentPlan(Long studentId) {
        log.info("getStudentPlan called - studentId: {}", studentId);
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        if (student.getFeePlanId() == null) {
            throw new ResourceNotFoundException("No fee plan assigned to student");
        }
        return toResponse(feePlanRepository.findById(student.getFeePlanId())
            .orElseThrow(() -> new ResourceNotFoundException("Fee plan not found")));
    }

    private FeePlanResponse toResponse(FeePlan plan) {
        var items = plan.getItems() == null ? List.<FeePlanResponse.FeePlanItemResponse>of() :
            plan.getItems().stream()
                .map(i -> new FeePlanResponse.FeePlanItemResponse(
                    i.getId(), i.getFeeType(), i.getAmount(), i.getFrequency()))
                .toList();
        return new FeePlanResponse(plan.getId(), plan.getName(),
            plan.getDescription(), plan.isActive(), items);
    }
}
