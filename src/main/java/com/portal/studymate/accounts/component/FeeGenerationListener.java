package com.portal.studymate.accounts.component;

import com.portal.studymate.accounts.service.StudentFeeService;
import com.portal.studymate.accounts.dtos.StudentEnrolledEvent;
import com.portal.studymate.exam.dto.ExamCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeeGenerationListener {

   private final StudentFeeService studentFeeService;

   @EventListener
   public void handleStudentEnrolled(StudentEnrolledEvent event) {
      log.info("Generating fees for enrollment: {}", event.enrollmentId());
      
      try {
         studentFeeService.generateFeesForEnrollment(event.enrollmentId());
         log.info("Successfully generated fees for enrollment: {}", event.enrollmentId());
      } catch (Exception e) {
         log.error("Failed to generate fees for enrollment: {}", event.enrollmentId(), e);
      }
   }

   @EventListener
   public void handleExamCreated(ExamCreatedEvent event) {
      log.info("Generating exam fees for exam: {}", event.examId());
      
      try {
         studentFeeService.generateExamFeesForAllStudents(event.examId());
         log.info("Successfully generated exam fees for exam: {}", event.examId());
      } catch (Exception e) {
         log.error("Failed to generate exam fees for exam: {}", event.examId(), e);
      }
   }
}