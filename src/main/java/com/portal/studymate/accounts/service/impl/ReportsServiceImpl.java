package com.portal.studymate.accounts.service.impl;

import com.portal.studymate.accounts.dtos.responses.*;
import com.portal.studymate.accounts.repository.PaymentRepository;
import com.portal.studymate.accounts.repository.StudentFeeRepository;
import com.portal.studymate.accounts.service.ReportsService;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {

   private final StudentFeeRepository studentFeeRepository;
   private final PaymentRepository paymentRepository;
   private final AcademicYearRepository academicYearRepository;

   @Override
   public AccountsDashboardResponse getAccountsDashboard(Long academicYearId) {
      log.info("getAccountsDashboard called - academicYearId: {}", academicYearId);
      School school = SchoolContext.getSchool();
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      BigDecimal totalCollection = studentFeeRepository.getTotalCollectionBySchoolAndAcademicYear(school, academicYear);
      BigDecimal pendingAmount = studentFeeRepository.getTotalPendingBySchoolAndAcademicYear(school, academicYear);
      Long totalStudents = studentFeeRepository.getTotalStudentsBySchoolAndAcademicYear(school, academicYear);
      
      BigDecimal totalAmount = totalCollection.add(pendingAmount);
      BigDecimal collectionPercentage = totalAmount.compareTo(BigDecimal.ZERO) > 0 
         ? totalCollection.divide(totalAmount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
         : BigDecimal.ZERO;

      return new AccountsDashboardResponse(
         totalCollection,
         pendingAmount,
         collectionPercentage,
         totalStudents,
         0L, // TODO: Calculate paid students
         0L  // TODO: Calculate pending students
      );
   }

   @Override
   public DailyCollectionReportResponse getDailyCollectionReport(LocalDate date) {
      log.info("getDailyCollectionReport called - date: {}", date);
      School school = SchoolContext.getSchool();
      
      BigDecimal totalCollection = paymentRepository.getDailyCollectionBySchoolAndDate(school, date);
      Long totalPayments = paymentRepository.getDailyPaymentCountBySchoolAndDate(school, date);

      return new DailyCollectionReportResponse(
         date.toString(),
         totalCollection,
         totalPayments
      );
   }

   @Override
   public OutstandingFeesReportResponse getOutstandingFeesReport(Long academicYearId) {
      log.info("getOutstandingFeesReport called - academicYearId: {}", academicYearId);
      School school = SchoolContext.getSchool();
      AcademicYear academicYear = academicYearRepository.findById(academicYearId)
         .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

      BigDecimal totalOutstanding = studentFeeRepository.getTotalPendingBySchoolAndAcademicYear(school, academicYear);
      
      return new OutstandingFeesReportResponse(
         totalOutstanding,
         0L, // TODO: Calculate students with pending
         new OutstandingFeesReportResponse.ClassWiseOutstanding("", BigDecimal.ZERO, 0L)
      );
   }

   @Override
   public List<FeeCollectionTrendResponse> getFeeCollectionTrend(LocalDate startDate, LocalDate endDate) {
      log.info("getFeeCollectionTrend called - range: {} to {}", startDate, endDate);
      // TODO: Implement collection trend logic
      return List.of();
   }
}