package com.portal.studymate.school.controller;

import com.portal.studymate.school.dtos.DashboardSummaryResponse;
import com.portal.studymate.school.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

   private final DashboardService dashboardService;

   @GetMapping("/summary")
   public DashboardSummaryResponse getSummary() {
      return dashboardService.getSummary();
   }
}

