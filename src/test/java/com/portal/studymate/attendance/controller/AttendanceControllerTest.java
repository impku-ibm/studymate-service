package com.portal.studymate.attendance.controller;

import com.portal.studymate.attendance.dto.AttendanceResponse;
import com.portal.studymate.attendance.dto.AttendanceSummary;
import com.portal.studymate.attendance.dto.MarkAttendanceRequest;
import com.portal.studymate.attendance.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock private AttendanceService attendanceService;
    @InjectMocks private AttendanceController controller;

    @Test
    void markAttendance_returnsOk() {
        doNothing().when(attendanceService).markAttendance(any(MarkAttendanceRequest.class), any());
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> result = controller.markAttendance(new MarkAttendanceRequest(1L, "A", LocalDate.now(), List.of()), auth);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void getAttendanceByDate_returnsOk() {
        when(attendanceService.getAttendanceByDate(any(LocalDate.class), eq("A")))
            .thenReturn(List.of());

        ResponseEntity<List<AttendanceResponse>> result =
            controller.getAttendanceByDate(LocalDate.now(), "A");
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void getStudentAttendanceSummary_returnsOk() {
        AttendanceSummary summary = mock(AttendanceSummary.class);
        when(attendanceService.getStudentAttendanceSummary(eq(1L), any(YearMonth.class)))
            .thenReturn(summary);

        ResponseEntity<AttendanceSummary> result =
            controller.getStudentAttendanceSummary(1L, YearMonth.now());
        assertEquals(200, result.getStatusCode().value());
    }
}
