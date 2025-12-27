package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.SubjectCreateRequest;
import com.portal.studymate.schoolmodule.dtos.SubjectResponse;
import com.portal.studymate.schoolmodule.dtos.SubjectUpdateRequest;
import com.portal.studymate.schoolmodule.service.SubjectMasterService;
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
@RequestMapping("/school/subjects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SubjectMasterController {
   private final SubjectMasterService service;

   @PostMapping
   public ResponseEntity<SubjectResponse> create(
      @Valid @RequestBody SubjectCreateRequest request) {
      return ResponseEntity.ok(service.create(request));
   }

   @GetMapping
   public ResponseEntity<List<SubjectResponse>> getAll() {
      return ResponseEntity.ok(service.getAllActive());
   }

   @PutMapping("/{id}")
   public ResponseEntity<SubjectResponse> update(
      @PathVariable Long id,
      @Valid @RequestBody SubjectUpdateRequest request) {
      return ResponseEntity.ok(service.update(id, request));
   }
}
