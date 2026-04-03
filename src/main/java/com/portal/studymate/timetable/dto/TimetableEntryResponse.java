package com.portal.studymate.timetable.dto;

import java.time.LocalTime;

public record TimetableEntryResponse(
    Long id,
    Integer periodNumber,
    LocalTime startTime,
    LocalTime endTime,
    String className,
    String section,
    String subjectName,
    String teacherName,
    Integer dayOfWeek
) {}
