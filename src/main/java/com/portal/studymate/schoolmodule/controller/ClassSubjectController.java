package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.AssignSubjectRequest;
import com.portal.studymate.schoolmodule.dtos.ClassSubjectResponse;
import com.portal.studymate.schoolmodule.service.ClassSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school/classes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ClassSubjectController {
   private final ClassSubjectService service;

   @PostMapping("/{classId}/sections/{sectionId}/subjects")
   public ResponseEntity<String> assign(
      @PathVariable Long classId,
      @PathVariable Long sectionId,
      @RequestBody List<AssignSubjectRequest> subjects
   ) {
      service.assignSubjects(classId, sectionId, subjects);
      return ResponseEntity.ok("Subjects assigned successfully");
   }

   @GetMapping("/{classId}/sections/{sectionId}/subjects")
   public List<ClassSubjectResponse> get(
      @PathVariable Long classId,
      @PathVariable Long sectionId
   ) {
      return service.getSubjects(classId, sectionId);
   }
}
