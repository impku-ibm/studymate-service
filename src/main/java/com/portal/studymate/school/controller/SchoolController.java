package com.portal.studymate.school.controller;

import com.portal.studymate.school.dtos.CreateSchoolRequest;
import com.portal.studymate.school.dtos.SchoolResponse;
import com.portal.studymate.school.dtos.UpdateSchoolRequest;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

   private final SchoolService schoolService;

   /**
    * Get current school details
    */
   @GetMapping
   public SchoolResponse getSchool() {
      return schoolService.getCurrentSchool();
   }

   /**
    * One-time school setup
    * (should be allowed only for ADMIN)
    */
   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public SchoolResponse createSchool(@Valid @RequestBody CreateSchoolRequest school) {
      return schoolService.createSchool(school);
   }

   /**
    * Update school profile
    */
   @PutMapping
   public SchoolResponse updateSchool(@Valid @RequestBody UpdateSchoolRequest school) {
      return schoolService.updateSchool(school);
   }
}

