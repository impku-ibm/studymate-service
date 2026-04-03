package com.portal.studymate.accounts.model;

import com.portal.studymate.accounts.enums.PaymentMode;
import com.portal.studymate.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "student_id", nullable = false)
   private Student student;

   @Column(nullable = false, unique = true)
   private String receiptNumber;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private PaymentMode paymentMode;

   @Column(nullable = false)
   private BigDecimal totalAmount;

   @Column(nullable = false)
   private LocalDate paymentDate;

   private String transactionReference;

   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "created_by")
   private String createdBy;
}