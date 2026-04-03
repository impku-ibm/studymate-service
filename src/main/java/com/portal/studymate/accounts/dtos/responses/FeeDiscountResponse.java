package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.DiscountType;
import java.math.BigDecimal;

public record FeeDiscountResponse(
    Long id,
    Long studentId,
    String studentName,
    DiscountType discountType,
    BigDecimal percentage,
    BigDecimal fixedAmount,
    String reason,
    boolean active
) {}
