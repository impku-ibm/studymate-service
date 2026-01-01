package com.portal.studymate.classmanagement.controller;

import com.portal.studymate.classmanagement.dto.CreateSectionRequest;
import com.portal.studymate.classmanagement.dto.SectionResponse;
import com.portal.studymate.classmanagement.dto.UpdateSectionRequest;
import com.portal.studymate.classmanagement.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SectionController {

   private final SectionService sectionService;

   @PostMapping("/classes/{classId}/sections")
   @ResponseStatus(HttpStatus.CREATED)
   public SectionResponse createSection(
      @PathVariable Long classId,
      @Valid @RequestBody CreateSectionRequest request) {
      return sectionService.createSection(classId, request);
   }

   @GetMapping("/classes/{classId}/sections")
   public List<SectionResponse> getSections(
      @PathVariable Long classId) {
      return sectionService.getSectionsByClass(classId);
   }

   @PutMapping("/sections/{sectionId}")
   public SectionResponse updateSection(
      @PathVariable Long sectionId,
      @Valid @RequestBody UpdateSectionRequest request) {
      return sectionService.updateSection(sectionId, request);
   }
}

