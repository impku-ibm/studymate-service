package com.portal.studymate.attendance.dto;

import com.portal.studymate.attendance.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record MarkAttendanceRequest(
    @NotNull Long classId,
    @NotBlank String section,
    @NotNull LocalDate attendanceDate,
    @NotNull List<StudentAttendanceEntry> attendanceEntries
) {
    public record StudentAttendanceEntry(
        @NotNull Long studentId,
        @NotNull AttendanceStatus status
    ) {}
}