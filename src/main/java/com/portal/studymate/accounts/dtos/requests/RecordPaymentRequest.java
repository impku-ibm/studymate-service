package com.portal.studymate.accounts.dtos.requests;

import com.portal.studymate.accounts.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RecordPaymentRequest(

   @NotNull
   Long studentId,

   @NotNull
   List<StudentFeePayment> feePayments,

   @NotNull
   PaymentMode paymentMode,

   String transactionReference,

   LocalDate paymentDate
) {
   public record StudentFeePayment(
      @NotNull Long studentFeeId,
      @NotNull @Positive BigDecimal amount
   ) {}

   @Positive
   public BigDecimal getTotalAmount() {
      return feePayments.stream()
         .map(StudentFeePayment::amount)
         .reduce(BigDecimal.ZERO, BigDecimal::add);
   }
}

