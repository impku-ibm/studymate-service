package com.portal.studymate.accounts.dtos.responses;

import java.math.BigDecimal;

public record DailyCollectionReportResponse(
   String date,
   BigDecimal totalCollection,
   Long totalPayments
) {}