package com.portal.studymate.accounts.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransportEstimationRequest(
    @NotBlank String distanceSlab,
    @NotNull @Positive BigDecimal minFee,
    @NotNull @Positive BigDecimal maxFee,
    String busRouteName,
    String pickupZone
) {}