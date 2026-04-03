package com.portal.studymate.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeeSchedulerService {

    // Monthly fee generation will be implemented when StudentFeeService
    // is enhanced to use FeePlan. For now, this is the scheduled job skeleton.

    @Scheduled(cron = "0 0 1 1 * *") // 1 AM on 1st of each month
    public void generateMonthlyFeesForAllSchools() {
        log.info("Monthly fee generation job started");
        // TODO: Iterate all schools, get active enrollments,
        // generate fees per student's fee plan using FeeDiscountService
        log.info("Monthly fee generation job completed");
    }
}
