package com.portal.studymate.accounts.dtos.requests;

import com.portal.studymate.accounts.enums.DiscountType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateFeeDiscountRequest(
    @NotNull Long studentId,
    @NotNull Long academicYearId,
    @NotNull DiscountType discountType,
    BigDecimal percentage,
    BigDecimal fixedAmount,
    String reason
) {}
