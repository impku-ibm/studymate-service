package com.portal.studymate.attendance.dto;

import com.portal.studymate.attendance.enums.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceResponse(
    Long id,
    Long studentId,
    String studentName,
    LocalDate attendanceDate,
    AttendanceStatus status,
    String section,
    String markedByTeacher
) {}