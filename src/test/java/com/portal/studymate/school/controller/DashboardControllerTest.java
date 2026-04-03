package com.portal.studymate.school.controller;

import com.portal.studymate.school.dtos.DashboardSummaryResponse;
import com.portal.studymate.school.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock private DashboardService dashboardService;
    @InjectMocks private DashboardController controller;

    @Test
    void getSummary_callsService() {
        DashboardSummaryResponse response = DashboardSummaryResponse.builder()
            .totalStudents(100).totalTeachers(20).totalClasses(10)
            .activeAcademicYear("2024-2025").build();
        when(dashboardService.getSummary()).thenReturn(response);

        DashboardSummaryResponse result = controller.getSummary();
        assertNotNull(result);
        assertEquals(100, result.getTotalStudents());
    }

    @Test
    void getSummary_verifiesServiceCall() {
        when(dashboardService.getSummary()).thenReturn(DashboardSummaryResponse.builder().build());
        controller.getSummary();
        verify(dashboardService).getSummary();
    }
}
