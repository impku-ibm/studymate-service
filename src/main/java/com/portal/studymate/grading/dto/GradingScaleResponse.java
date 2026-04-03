package com.portal.studymate.grading.dto;

import java.math.BigDecimal;
import java.util.List;

public record GradingScaleResponse(
    Long id,
    String name,
    boolean isDefault,
    boolean active,
    List<GradingEntryResponse> entries
) {
    public record GradingEntryResponse(
        Long id,
        String gradeName,
        BigDecimal minPercentage,
        BigDecimal maxPercentage,
        BigDecimal gradePoint,
        String description
    ) {}
}
