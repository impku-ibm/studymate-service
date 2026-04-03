package com.portal.studymate.teacherassignment.controller;

import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;
import com.portal.studymate.teacherassignment.service.TeacherAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teacher-assignments")
@RequiredArgsConstructor
public class TeacherAssignmentController {

   private final TeacherAssignmentService teacherAssignmentService;

   @PostMapping
   public TeacherAssignmentResponse assignTeacher(
      @Valid @RequestBody CreateTeacherAssignmentRequest request
   ) {
      log.info("POST /teacher-assignments - Assigning teacher");
      return teacherAssignmentService.assignTeacher(request);
   }

   @GetMapping("/sections/{sectionId}")
   public List<TeacherAssignmentResponse>
   getAssignmentsForSection(
      @PathVariable Long sectionId
   ) {
      log.info("GET /teacher-assignments/sections/{} - Getting assignments", sectionId);
      return teacherAssignmentService
                .getAssignmentsForSection(sectionId);
   }

   @GetMapping("/me")
   public List<TeacherAssignmentResponse>
   getMyAssignments() {
      log.info("GET /teacher-assignments/me - Getting my assignments");
      return teacherAssignmentService
                .getAssignmentsForTeacher();
   }
}
