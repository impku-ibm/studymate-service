package com.portal.studymate.school.service;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.school.dtos.DashboardSummaryResponse;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.service.impl.DashboardServiceImpl;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private SchoolClassRepository classRepository;
    @Mock private AcademicYearRepository academicYearRepository;

    @InjectMocks private DashboardServiceImpl dashboardService;

    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").build();
    }

    @Test
    void getSummary_returnsCorrectCounts() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(studentRepository.countBySchool(school)).thenReturn(100L);
            when(teacherRepository.countBySchool(school)).thenReturn(20L);
            when(classRepository.countBySchool(school)).thenReturn(10L);

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-2025").build();
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.of(ay));

            DashboardSummaryResponse result = dashboardService.getSummary();
            assertEquals(100L, result.getTotalStudents());
            assertEquals(20L, result.getTotalTeachers());
            assertEquals(10L, result.getTotalClasses());
            assertEquals("2024-2025", result.getActiveAcademicYear());
        }
    }

    @Test
    void getSummary_noActiveYear_returnsDash() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(studentRepository.countBySchool(school)).thenReturn(0L);
            when(teacherRepository.countBySchool(school)).thenReturn(0L);
            when(classRepository.countBySchool(school)).thenReturn(0L);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.empty());

            DashboardSummaryResponse result = dashboardService.getSummary();
            assertEquals("-", result.getActiveAcademicYear());
        }
    }

    @Test
    void getSummary_nullSchool_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(null);
            assertThrows(IllegalStateException.class, () -> dashboardService.getSummary());
        }
    }
}
