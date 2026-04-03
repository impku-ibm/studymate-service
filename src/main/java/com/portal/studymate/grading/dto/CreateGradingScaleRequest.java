package com.portal.studymate.grading.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateGradingScaleRequest(
    @NotBlank String name,
    boolean isDefault,
    @NotNull List<GradingEntryRequest> entries
) {
    public record GradingEntryRequest(
        @NotBlank String gradeName,
        @NotNull BigDecimal minPercentage,
        @NotNull BigDecimal maxPercentage,
        BigDecimal gradePoint,
        String description
    ) {}
}
