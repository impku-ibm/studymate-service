package com.portal.studymate.exam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record GraceMarkRequest(
    @NotNull List<GraceMarkEntry> entries
) {
    public record GraceMarkEntry(
        @NotNull Long studentId,
        @NotNull Long examScheduleId,
        @NotNull @Positive Integer additionalMarks
    ) {}
}
