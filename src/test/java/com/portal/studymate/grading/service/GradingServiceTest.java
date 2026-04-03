package com.portal.studymate.grading.service;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.grading.model.GradingScale;
import com.portal.studymate.grading.model.GradingScaleEntry;
import com.portal.studymate.grading.repository.GradingScaleRepository;
import com.portal.studymate.school.model.School;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradingServiceTest {

    @Mock
    private GradingScaleRepository gradingScaleRepository;

    @InjectMocks
    private GradingService gradingService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("Test School");
    }

    @Test
    void calculateGrade_withConfiguredScale_returnsCorrectGrade() {
        GradingScale scale = new GradingScale();
        scale.setId(1L);
        scale.setDefault(true);

        GradingScaleEntry entryA1 = new GradingScaleEntry();
        entryA1.setGradeName("A1");
        entryA1.setMinPercentage(BigDecimal.valueOf(91));
        entryA1.setMaxPercentage(BigDecimal.valueOf(100));

        GradingScaleEntry entryA2 = new GradingScaleEntry();
        entryA2.setGradeName("A2");
        entryA2.setMinPercentage(BigDecimal.valueOf(81));
        entryA2.setMaxPercentage(BigDecimal.valueOf(90));

        scale.setEntries(List.of(entryA1, entryA2));

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(gradingScaleRepository.findBySchoolAndIsDefaultTrue(school))
                .thenReturn(Optional.of(scale));

            assertEquals("A1", gradingService.calculateGrade(1L, BigDecimal.valueOf(95)));
            assertEquals("A2", gradingService.calculateGrade(1L, BigDecimal.valueOf(85)));
        }
    }

    @Test
    void calculateGrade_withoutConfiguredScale_returnsCBSEFallback() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(gradingScaleRepository.findBySchoolAndIsDefaultTrue(school))
                .thenReturn(Optional.empty());

            assertEquals("A1", gradingService.calculateGrade(1L, BigDecimal.valueOf(95)));
            assertEquals("B1", gradingService.calculateGrade(1L, BigDecimal.valueOf(75)));
            assertEquals("D", gradingService.calculateGrade(1L, BigDecimal.valueOf(35)));
            assertEquals("E", gradingService.calculateGrade(1L, BigDecimal.valueOf(20)));
        }
    }

    @Test
    void getGradingScales_returnsSchoolScales() {
        GradingScale scale = new GradingScale();
        scale.setId(1L);
        scale.setName("CBSE");
        scale.setDefault(true);
        scale.setActive(true);

        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(gradingScaleRepository.findBySchool(school)).thenReturn(List.of(scale));

            var result = gradingService.getGradingScales();
            assertEquals(1, result.size());
            assertEquals("CBSE", result.get(0).name());
        }
    }
}
