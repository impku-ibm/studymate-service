package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.student.ChangeSectionRequest;
import com.portal.studymate.schoolmodule.dtos.student.EnrollStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.PromoteStudentRequest;
import com.portal.studymate.schoolmodule.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/school/enrollments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StudentEnrollmentController {

   private final StudentEnrollmentService service;

   @PostMapping
   public ResponseEntity<String> enroll(
      @Valid @RequestBody EnrollStudentRequest req) {

      service.enroll(req);
      return ResponseEntity.ok("Student enrolled");
   }

   @PutMapping("/{id}/change-section")
   public ResponseEntity<String> changeSection(
      @PathVariable Long id,
      @Valid @RequestBody ChangeSectionRequest req) {

      service.changeSection(id, req);
      return ResponseEntity.ok("Section changed");
   }

   @PostMapping("/{id}/promote")
   public ResponseEntity<String> promote(
      @PathVariable Long id,
      @Valid @RequestBody PromoteStudentRequest req) {

      service.promote(id, req);
      return ResponseEntity.ok("Student promoted");
   }
}

