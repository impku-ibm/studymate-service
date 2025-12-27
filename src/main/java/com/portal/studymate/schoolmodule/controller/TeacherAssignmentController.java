package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.teacher.AssignTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherAssignmentResponse;
import com.portal.studymate.schoolmodule.service.TeacherAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school/teachers/assignments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TeacherAssignmentController {

   private final TeacherAssignmentService service;

   @PostMapping
   public ResponseEntity<String> assign(
      @Valid @RequestBody AssignTeacherRequest request) {

      service.assignTeacher(request);
      return ResponseEntity.ok("Teacher assigned successfully");
   }

   @GetMapping
   public List<TeacherAssignmentResponse> get(
      @RequestParam Long academicYearId) {

      return service.getAssignments(academicYearId);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<String> remove(@PathVariable Long id) {

      service.removeAssignment(id);
      return ResponseEntity.ok("Assignment removed successfully");
   }
}
