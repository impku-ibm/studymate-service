package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.EnrollStudentRequest;
import com.portal.studymate.student.dto.StudentEnrollmentResponse;
import com.portal.studymate.student.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

   private final StudentEnrollmentService service;

   @PostMapping
   public StudentEnrollmentResponse enroll(
      @RequestBody @Valid EnrollStudentRequest req) {
      log.info("POST /enrollments - Enrolling student");
      return service.enroll(req);
   }

   @GetMapping
   public List<StudentEnrollmentResponse> list() {
      log.info("GET /enrollments - Listing active year enrollments");
      return service.listActiveYear();
   }
}

