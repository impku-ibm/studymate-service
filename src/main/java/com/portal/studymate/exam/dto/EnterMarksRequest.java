package com.portal.studymate.exam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record EnterMarksRequest(
    @NotNull Long examScheduleId,
    @NotNull Long studentId,
    @Min(0) Integer marksObtained,
    Boolean absent,
    String remarks
) {}