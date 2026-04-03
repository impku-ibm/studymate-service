package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.accounts.dtos.requests.RecordPaymentRequest;
import com.portal.studymate.accounts.dtos.responses.PaymentResponse;
import com.portal.studymate.accounts.enums.AuditAction;
import com.portal.studymate.accounts.model.Payment;
import com.portal.studymate.accounts.model.PaymentDetail;
import com.portal.studymate.accounts.model.StudentFee;
import com.portal.studymate.accounts.repository.PaymentDetailRepository;
import com.portal.studymate.accounts.repository.PaymentRepository;
import com.portal.studymate.accounts.repository.StudentFeeRepository;
import com.portal.studymate.accounts.service.AuditService;
import com.portal.studymate.accounts.service.PaymentService;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

   private final PaymentRepository paymentRepository;
   private final PaymentDetailRepository paymentDetailRepository;
   private final StudentFeeRepository studentFeeRepository;
   private final StudentRepository studentRepository;
   private final AuditService auditService;
   private final AtomicLong receiptCounter = new AtomicLong(System.currentTimeMillis() % 10000);

   @Override
   public PaymentResponse recordPayment(RecordPaymentRequest request) {
      log.info("recordPayment called - studentId: {}", request.studentId());
      Student student = studentRepository.findById(request.studentId())
         .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

      Payment payment = new Payment();
      payment.setStudent(student);
      payment.setReceiptNumber(generateReceiptNumber());
      payment.setPaymentMode(request.paymentMode());
      payment.setTotalAmount(request.getTotalAmount());
      payment.setPaymentDate(request.paymentDate() != null ? request.paymentDate() : LocalDate.now());
      payment.setTransactionReference(request.transactionReference());
      payment.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

      payment = paymentRepository.save(payment);

      for (RecordPaymentRequest.StudentFeePayment feePayment : request.feePayments()) {
         StudentFee studentFee = studentFeeRepository.findById(feePayment.studentFeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Student fee not found"));

         studentFee.applyPayment(feePayment.amount());
         studentFeeRepository.save(studentFee);

         PaymentDetail detail = new PaymentDetail();
         detail.setPayment(payment);
         detail.setStudentFee(studentFee);
         detail.setAmountPaid(feePayment.amount());

         paymentDetailRepository.save(detail);

         auditService.logAction(AuditAction.PAYMENT_RECORDED, "Payment", payment.getId(),
            "Payment recorded for " + student.getFullName() + " - Amount: " + feePayment.amount());
      }

      return buildPaymentResponse(payment);
   }

   @Override
   @Transactional(readOnly = true)
   public PaymentResponse getPaymentByReceipt(String receiptNumber) {
      log.info("getPaymentByReceipt called - receiptNumber: {}", receiptNumber);
      Payment payment = paymentRepository.findByReceiptNumber(receiptNumber)
         .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

      return buildPaymentResponse(payment);
   }

   private PaymentResponse buildPaymentResponse(Payment payment) {
      List<PaymentDetail> details = paymentDetailRepository.findByPayment(payment);
      
      List<PaymentResponse.PaymentDetailResponse> detailResponses = details.stream()
         .map(detail -> new PaymentResponse.PaymentDetailResponse(
            detail.getStudentFee().getFeeStructure().getFeeType().name(),
            detail.getAmountPaid()
         ))
         .toList();

      return new PaymentResponse(
         payment.getId(),
         payment.getReceiptNumber(),
         payment.getStudent().getFullName(),
         payment.getStudent().getAdmissionNumber(),
         payment.getPaymentMode(),
         payment.getTotalAmount(),
         payment.getPaymentDate(),
         payment.getTransactionReference(),
         detailResponses
      );
   }

   private String generateReceiptNumber() {
      String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
      return "RCP" + date + String.format("%04d", receiptCounter.incrementAndGet());
   }
}