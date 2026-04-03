package com.portal.studymate.promotion.dto;

import java.util.List;

public record PromotionResultResponse(
    int promotedCount,
    int skippedCount,
    List<String> errors
) {}
