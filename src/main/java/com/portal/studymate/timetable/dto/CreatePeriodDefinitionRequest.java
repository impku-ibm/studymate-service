package com.portal.studymate.timetable.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreatePeriodDefinitionRequest(
    @NotNull Integer periodNumber,
    @NotNull LocalTime startTime,
    @NotNull LocalTime endTime,
    boolean isBreak,
    String label
) {}
