package com.portal.studymate.accounts.dtos.requests;

import com.portal.studymate.accounts.enums.FeeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateFeeStructureRequest(
   @NotNull Long academicYearId,
   @NotNull Long classId,
   @NotNull FeeType feeType,
   @NotNull @Positive BigDecimal amount,
   @NotNull LocalDate dueDate
) {}