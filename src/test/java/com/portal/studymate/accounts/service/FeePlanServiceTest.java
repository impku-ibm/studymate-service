package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.CreateFeePlanRequest;
import com.portal.studymate.accounts.enums.FeeFrequency;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.model.FeePlan;
import com.portal.studymate.accounts.repository.FeePlanRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeePlanServiceTest {

    @Mock private FeePlanRepository feePlanRepository;
    @Mock private StudentRepository studentRepository;

    @InjectMocks private FeePlanService feePlanService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("Test School");
    }

    @Test
    void createFeePlan_success() {
        var itemReq = new CreateFeePlanRequest.FeePlanItemRequest(
            FeeType.TUITION, BigDecimal.valueOf(5000), FeeFrequency.MONTHLY);
        var request = new CreateFeePlanRequest("Day Scholar", "Basic plan", List.of(itemReq));

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(feePlanRepository.save(any(FeePlan.class))).thenAnswer(inv -> {
                FeePlan plan = inv.getArgument(0);
                plan.setId(1L);
                return plan;
            });

            var result = feePlanService.createFeePlan(request);
            assertNotNull(result);
            assertEquals("Day Scholar", result.name());
            verify(feePlanRepository).save(any(FeePlan.class));
        }
    }

    @Test
    void getFeePlans_returnsSchoolPlans() {
        FeePlan plan = FeePlan.builder().id(1L).school(school).name("Hosteller").active(true).build();

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(feePlanRepository.findBySchool(school)).thenReturn(List.of(plan));

            var result = feePlanService.getFeePlans();
            assertEquals(1, result.size());
            assertEquals("Hosteller", result.get(0).name());
        }
    }

    @Test
    void assignPlanToStudent_success() {
        Student student = Student.builder().id(1L).school(school).fullName("Test Student").build();
        FeePlan plan = FeePlan.builder().id(1L).school(school).name("Day Scholar").build();

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(feePlanRepository.findById(1L)).thenReturn(Optional.of(plan));
            when(studentRepository.save(any())).thenReturn(student);

            feePlanService.assignPlanToStudent(1L, 1L);
            assertEquals(1L, student.getFeePlanId());
            verify(studentRepository).save(student);
        }
    }

    @Test
    void getStudentPlan_throwsWhenNoPlan() {
        Student student = Student.builder().id(1L).school(school).fullName("Test").build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(ResourceNotFoundException.class,
            () -> feePlanService.getStudentPlan(1L));
    }
}
