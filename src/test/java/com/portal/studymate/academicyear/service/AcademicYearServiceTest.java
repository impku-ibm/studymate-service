package com.portal.studymate.academicyear.service;

import com.portal.studymate.academicyear.dto.AcademicYearResponse;
import com.portal.studymate.academicyear.dto.CreateAcademicYearRequest;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.academicyear.service.impl.AcademicYearServiceImpl;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicYearServiceTest {

    @Mock private AcademicYearRepository academicYearRepository;
    @InjectMocks private AcademicYearServiceImpl academicYearService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("Test School");
    }

    @Test
    void createAcademicYear_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndYear(school, "2024-2025")).thenReturn(Optional.empty());
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)).thenReturn(Optional.empty());
            when(academicYearRepository.save(any(AcademicYear.class))).thenAnswer(inv -> {
                AcademicYear ay = inv.getArgument(0);
                ay.setId(1L);
                return ay;
            });

            CreateAcademicYearRequest request = new CreateAcademicYearRequest();
            request.setStartYear(2024);

            AcademicYearResponse result = academicYearService.createAcademicYear(request);
            assertNotNull(result);
            assertEquals("2024-2025", result.getYear());
        }
    }

    @Test
    void activateAcademicYear_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            AcademicYear target = AcademicYear.builder().id(2L).school(school).year("2025-2026")
                .status(AcademicYearStatus.COMPLETED).active(false).build();
            when(academicYearRepository.findById(2L)).thenReturn(Optional.of(target));
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)).thenReturn(Optional.empty());
            when(academicYearRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            AcademicYearResponse result = academicYearService.activateAcademicYear(2L);
            assertEquals("ACTIVE", result.getStatus());
        }
    }

    @Test
    void getActiveAcademicYear_notFound_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> academicYearService.getActiveAcademicYear());
        }
    }

    @Test
    void getAllAcademicYears_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-2025").status(AcademicYearStatus.ACTIVE).build();
            when(academicYearRepository.findBySchoolOrderByCreatedAtDesc(school)).thenReturn(List.of(ay));

            List<AcademicYearResponse> result = academicYearService.getAllAcademicYears();
            assertEquals(1, result.size());
            assertEquals("2024-2025", result.get(0).getYear());
        }
    }
}
