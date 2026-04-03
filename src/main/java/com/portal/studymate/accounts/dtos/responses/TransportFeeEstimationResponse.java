package com.portal.studymate.accounts.dtos.responses;

import java.math.BigDecimal;

public record TransportFeeEstimationResponse(
    Long id,
    String distanceSlab,
    BigDecimal minFee,
    BigDecimal maxFee,
    String busRouteName,
    String pickupZone,
    BigDecimal suggestedFee
) {}