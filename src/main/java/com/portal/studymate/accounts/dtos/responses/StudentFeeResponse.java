package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.StudentFeeStatus;
import com.portal.studymate.accounts.model.StudentFee;

import java.math.BigDecimal;
import java.time.LocalDate;


public record StudentFeeResponse(

   Long id,

   Long studentId,
   String studentName,
   String admissionNumber,

   String className,

   String feeType,

   BigDecimal totalAmount,
   BigDecimal paidAmount,
   BigDecimal pendingAmount,

   LocalDate dueDate,

   StudentFeeStatus status
) {

   public static StudentFeeResponse from(StudentFee fee) {

      return new StudentFeeResponse(
         fee.getId(),

         fee.getStudent().getId(),
         fee.getStudent().getFullName(),
         fee.getStudent().getAdmissionNumber(),

         fee.getFeeStructure().getSchoolClass().getName(),
         fee.getFeeStructure().getFeeType().name(),

         fee.getTotalAmount(),
         fee.getPaidAmount(),
         fee.getPendingAmount(),

         fee.getDueDate(),

         fee.getStatus()
      );
   }
}


