package com.portal.studymate.teacherassignment.controller;

import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;
import com.portal.studymate.teacherassignment.service.TeacherAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-assignments")
@RequiredArgsConstructor
public class TeacherAssignmentController {

   private final TeacherAssignmentService teacherAssignmentService;

   @PostMapping
   public TeacherAssignmentResponse assignTeacher(
      @Valid @RequestBody CreateTeacherAssignmentRequest request
   ) {
      return teacherAssignmentService.assignTeacher(request);
   }

   @GetMapping("/sections/{sectionId}")
   public List<TeacherAssignmentResponse>
   getAssignmentsForSection(
      @PathVariable Long sectionId
   ) {
      return teacherAssignmentService
                .getAssignmentsForSection(sectionId);
   }

   @GetMapping("/me")
   public List<TeacherAssignmentResponse>
   getMyAssignments() {
      return teacherAssignmentService
                .getAssignmentsForTeacher();
   }
}
