package com.portal.studymate.promotion.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BulkPromotionRequest(
    @NotNull Long sourceClassId,
    @NotNull String sourceSection,
    @NotNull Long targetClassId,
    @NotNull String targetSection,
    @NotNull Long targetAcademicYearId,
    @NotEmpty List<Long> studentIds
) {}
