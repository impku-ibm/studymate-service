package com.portal.studymate.accounts.dtos.responses;

import java.math.BigDecimal;

public record FeeCollectionTrendResponse(
   String date,
   BigDecimal amount
) {}