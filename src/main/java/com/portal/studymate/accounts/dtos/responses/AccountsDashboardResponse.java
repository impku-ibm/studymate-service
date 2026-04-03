package com.portal.studymate.accounts.dtos.responses;

import java.math.BigDecimal;

public record AccountsDashboardResponse(
   BigDecimal totalCollection,
   BigDecimal pendingAmount,
   BigDecimal collectionPercentage,
   Long totalStudents,
   Long paidStudents,
   Long pendingStudents
) {}