package com.portal.studymate.accounts.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetail {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "payment_id", nullable = false)
   private Payment payment;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "student_fee_id", nullable = false)
   private StudentFee studentFee;

   @Column(nullable = false)
   private BigDecimal amountPaid;
}