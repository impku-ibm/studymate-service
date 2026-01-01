package com.portal.studymate.subject.controller;

import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;
import com.portal.studymate.subject.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

   private final SubjectService subjectService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public SubjectResponse createSubject(
      @Valid @RequestBody CreateSubjectRequest request) {
      return subjectService.createSubject(request);
   }

   @GetMapping
   public List<SubjectResponse> getAllSubjects() {
      return subjectService.getAllSubjects();
   }

   @PutMapping("/{subjectId}")
   public SubjectResponse updateSubject(
      @PathVariable Long subjectId,
      @Valid @RequestBody UpdateSubjectRequest request) {
      return subjectService.updateSubject(subjectId, request);
   }
}

