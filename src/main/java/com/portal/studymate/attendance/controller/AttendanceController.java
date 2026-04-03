package com.portal.studymate.attendance.controller;

import com.portal.studymate.attendance.dto.*;
import com.portal.studymate.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance Management", description = "APIs for managing student and teacher attendance")
public class AttendanceController {
    
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Mark attendance for students")
    public ResponseEntity<Void> markAttendance(@Valid @RequestBody MarkAttendanceRequest request,
                                              Authentication authentication) {
        Long teacherId = extractTeacherIdFromAuth(authentication);
        attendanceService.markAttendance(request, teacherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/date/{date}/section/{section}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Get attendance by date and section")
    public ResponseEntity<List<AttendanceResponse>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String section) {
        var attendance = attendanceService.getAttendanceByDate(date, section);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}/summary")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    @Operation(summary = "Get student attendance summary for a month")
    public ResponseEntity<AttendanceSummary> getStudentAttendanceSummary(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        var summary = attendanceService.getStudentAttendanceSummary(studentId, month);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/student/{studentId}/history")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or hasRole('STUDENT')")
    @Operation(summary = "Get student attendance history")
    public ResponseEntity<List<AttendanceResponse>> getStudentAttendanceHistory(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var history = attendanceService.getStudentAttendanceHistory(studentId, startDate, endDate);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/teacher/self")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Teacher marks own attendance")
    public ResponseEntity<Void> markTeacherSelfAttendance(Authentication authentication) {
        Long teacherId = extractTeacherIdFromAuth(authentication);
        attendanceService.markTeacherSelfAttendance(teacherId);
        return ResponseEntity.ok().build();
    }

    private Long extractTeacherIdFromAuth(Authentication authentication) {
        // Implementation depends on your authentication setup
        return 1L; // Replace with actual teacher ID extraction
    }
}