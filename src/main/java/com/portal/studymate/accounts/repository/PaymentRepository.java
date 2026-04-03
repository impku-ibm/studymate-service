package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.Payment;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

   Optional<Payment> findByReceiptNumber(String receiptNumber);

   @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Payment p JOIN p.student s WHERE s.school = :school AND p.paymentDate = :date")
   BigDecimal getDailyCollectionBySchoolAndDate(
      @Param("school") School school,
      @Param("date") LocalDate date
   );

   @Query("SELECT COUNT(p) FROM Payment p JOIN p.student s WHERE s.school = :school AND p.paymentDate = :date")
   Long getDailyPaymentCountBySchoolAndDate(
      @Param("school") School school,
      @Param("date") LocalDate date
   );
}