package com.portal.studymate.subject.controller;

import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;
import com.portal.studymate.subject.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

   private final SubjectService subjectService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public SubjectResponse createSubject(
      @Valid @RequestBody CreateSubjectRequest request) {
      log.info("POST /subjects - Creating subject");
      return subjectService.createSubject(request);
   }

   @GetMapping
   public List<SubjectResponse> getAllSubjects() {
      log.info("GET /subjects - Getting all subjects");
      return subjectService.getAllSubjects();
   }

   @PutMapping("/{subjectId}")
   public SubjectResponse updateSubject(
      @PathVariable Long subjectId,
      @Valid @RequestBody UpdateSubjectRequest request) {
      log.info("PUT /subjects/{} - Updating subject", subjectId);
      return subjectService.updateSubject(subjectId, request);
   }
}

