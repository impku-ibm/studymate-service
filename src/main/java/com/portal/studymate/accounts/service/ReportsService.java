package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.responses.*;
import java.time.LocalDate;
import java.util.List;

public interface ReportsService {
   AccountsDashboardResponse getAccountsDashboard(Long academicYearId);
   DailyCollectionReportResponse getDailyCollectionReport(LocalDate date);
   OutstandingFeesReportResponse getOutstandingFeesReport(Long academicYearId);
   List<FeeCollectionTrendResponse> getFeeCollectionTrend(LocalDate startDate, LocalDate endDate);
}