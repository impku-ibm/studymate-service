package com.portal.studymate.accounts.dtos.responses;

import java.math.BigDecimal;

public record OutstandingFeesReportResponse(
   BigDecimal totalOutstanding,
   Long studentsWithPending,
   ClassWiseOutstanding classWiseData
) {
   public record ClassWiseOutstanding(
      String className,
      BigDecimal outstanding,
      Long studentCount
   ) {}
}

