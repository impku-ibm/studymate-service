package com.portal.studymate.attendance.dto;

import java.math.BigDecimal;

public record AttendanceSummary(
    Long studentId,
    String studentName,
    Integer totalDays,
    Integer presentDays,
    Integer absentDays,
    Integer leaveDays,
    BigDecimal attendancePercentage
) {}