package com.portal.studymate.accounts.model;

import com.portal.studymate.accounts.enums.StudentFeeStatus;
import com.portal.studymate.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
   name = "student_fee",
   uniqueConstraints = {
      @UniqueConstraint(columnNames = {"student_id", "fee_structure_id"})
   }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFee {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "student_id", nullable = false)
   private Student student;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "fee_structure_id", nullable = false)
   private FeeStructure feeStructure;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private StudentFeeStatus status = StudentFeeStatus.PENDING;

   @Column(nullable = false)
   private BigDecimal totalAmount;

   @Column(nullable = false)
   private BigDecimal paidAmount = BigDecimal.ZERO;

   @Column(nullable = false)
   private LocalDate dueDate;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at")
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   public void applyPayment(BigDecimal paymentAmount) {
      if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
         throw new IllegalArgumentException("Payment must be positive");
      }

      this.paidAmount = this.paidAmount.add(paymentAmount);

      if (this.paidAmount.compareTo(this.totalAmount) >= 0) {
         this.paidAmount = this.totalAmount;
         this.status = StudentFeeStatus.PAID;
      } else {
         this.status = StudentFeeStatus.PARTIAL_PAID;
      }
   }

   @Transient
   public BigDecimal getPendingAmount() {
      return totalAmount.subtract(paidAmount);
   }
}

