package com.portal.studymate.timetable.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTimetableEntryRequest(
    @NotNull Long periodDefinitionId,
    @NotNull Long classId,
    @NotBlank String section,
    Long subjectId,
    Long teacherId,
    @NotNull @Min(1) @Max(7) Integer dayOfWeek
) {}
