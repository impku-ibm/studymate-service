package com.portal.studymate.classsubject.controller;

import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;
import com.portal.studymate.classsubject.service.ClassSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

