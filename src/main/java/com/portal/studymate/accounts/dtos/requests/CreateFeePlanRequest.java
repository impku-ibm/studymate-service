package com.portal.studymate.accounts.dtos.requests;

import com.portal.studymate.accounts.enums.FeeFrequency;
import com.portal.studymate.accounts.enums.FeeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record CreateFeePlanRequest(
    @NotBlank String name,
    String description,
    @NotNull List<FeePlanItemRequest> items
) {
    public record FeePlanItemRequest(
        @NotNull FeeType feeType,
        @NotNull @Positive BigDecimal amount,
        @NotNull FeeFrequency frequency
    ) {}
}
