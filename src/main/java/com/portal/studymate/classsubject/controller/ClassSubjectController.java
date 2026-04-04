package com.portal.studymate.classsubject.controller;

import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;
import com.portal.studymate.classsubject.service.ClassSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/class-subjects")
@RequiredArgsConstructor
public class ClassSubjectController {

   private final ClassSubjectService classSubjectService;

   @PostMapping
   public List<ClassSubjectResponse> assignSubjects(
      @Valid @RequestBody AssignSubjectsRequest request) {
      return classSubjectService.assignSubjects(request);
   }

   @GetMapping("/classes/{classId}")
   public List<ClassSubjectResponse> getSubjectsForClass(
      @PathVariable Long classId) {
      return classSubjectService.getSubjectsForClass(classId);
   }

   // Also support query param style: GET /class-subjects?classId=X
   @GetMapping
   public List<ClassSubjectResponse> getSubjectsForClassByParam(
      @RequestParam Long classId,
      @RequestParam(required = false) Long academicYearId) {
      return classSubjectService.getSubjectsForClass(classId);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void removeSubjectMapping(@PathVariable Long id) {
      classSubjectService.removeMapping(id);
   }

   @PostMapping("/copy")
   public List<ClassSubjectResponse> copyFromClass(
      @RequestParam Long sourceClassId,
      @RequestParam Long targetClassId) {
      return classSubjectService.copySubjectsFromClass(sourceClassId, targetClassId);
   }
}

