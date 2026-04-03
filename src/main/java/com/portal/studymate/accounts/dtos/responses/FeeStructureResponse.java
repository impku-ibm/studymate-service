package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.FeeType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FeeStructureResponse(
   Long id,
   String academicYear,
   String className,
   FeeType feeType,
   BigDecimal amount,
   LocalDate dueDate,
   boolean active
) {}