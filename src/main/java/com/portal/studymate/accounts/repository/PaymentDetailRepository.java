package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.PaymentDetail;
import com.portal.studymate.accounts.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
   List<PaymentDetail> findByPayment(Payment payment);
}