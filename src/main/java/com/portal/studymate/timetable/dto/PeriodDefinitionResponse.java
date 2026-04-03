package com.portal.studymate.timetable.dto;

import java.time.LocalTime;

public record PeriodDefinitionResponse(
    Long id,
    Integer periodNumber,
    LocalTime startTime,
    LocalTime endTime,
    boolean isBreak,
    String label
) {}
