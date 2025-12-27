package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.student.CreateStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.StudentResponse;
import com.portal.studymate.schoolmodule.dtos.student.UpdateStudentRequest;
import com.portal.studymate.schoolmodule.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StudentController {

   private final StudentService service;

   @PostMapping
   public ResponseEntity<StudentResponse> create(
      @Valid @RequestBody CreateStudentRequest req) {
      return ResponseEntity.ok(service.create(req));
   }

   @GetMapping
   public List<StudentResponse> list() {
      return service.list();
   }

   @GetMapping("/{id}")
   public StudentResponse view(@PathVariable Long id) {
      return service.getById(id);
   }

   @PutMapping("/{id}")
   public StudentResponse update(
      @PathVariable Long id,
      @Valid @RequestBody UpdateStudentRequest req) {
      return service.update(id, req);
   }

   @PutMapping("/{id}/mark-left")
   public ResponseEntity<String> markLeft(@PathVariable Long id) {
      service.markLeft(id);
      return ResponseEntity.ok("Student marked as left");
   }
}
