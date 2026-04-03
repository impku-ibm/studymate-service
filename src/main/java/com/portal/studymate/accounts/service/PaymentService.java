package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.RecordPaymentRequest;
import com.portal.studymate.accounts.dtos.responses.PaymentResponse;

public interface PaymentService {
   PaymentResponse recordPayment(RecordPaymentRequest request);
   PaymentResponse getPaymentByReceipt(String receiptNumber);
}