package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.PaymentMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PaymentResponse(
   Long id,
   String receiptNumber,
   String studentName,
   String admissionNumber,
   PaymentMode paymentMode,
   BigDecimal totalAmount,
   LocalDate paymentDate,
   String transactionReference,
   List<PaymentDetailResponse> details
) {
   public record PaymentDetailResponse(
      String feeType,
      BigDecimal amountPaid
   ) {}
}