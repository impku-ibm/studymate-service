package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.FeeFrequency;
import com.portal.studymate.accounts.enums.FeeType;
import java.math.BigDecimal;
import java.util.List;

public record FeePlanResponse(
    Long id,
    String name,
    String description,
    boolean active,
    List<FeePlanItemResponse> items
) {
    public record FeePlanItemResponse(
        Long id,
        FeeType feeType,
        BigDecimal amount,
        FeeFrequency frequency
    ) {}
}
