package com.portal.studymate.user.controller;

import com.portal.studymate.user.dtos.CreateAcademicYearRequest;
import com.portal.studymate.user.dtos.CreateSchoolRequest;
import com.portal.studymate.user.dtos.SchoolMapper;
import com.portal.studymate.user.dtos.SchoolResponse;
import com.portal.studymate.user.model.School;
import com.portal.studymate.user.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user/schools")
@RequiredArgsConstructor
@Slf4j
public class SchoolController {
   private final SchoolService service;
   private final SchoolMapper mapper;

   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping
   public ResponseEntity<SchoolResponse> create(
      @Valid @RequestBody CreateSchoolRequest req,
      Authentication auth
   ) {
      log.info("Creating school - User: {}, School: {}", auth.getName(), req.name());
      School school = service.createSchool(mapper.toEntity(req));
      log.info("School created successfully - ID: {}, Name: {}", school.getId(), school.getName());
      return ResponseEntity.ok(mapper.toResponse(school));
   }

   @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL')")
   @PostMapping("/{schoolId}/academic-years")
   public ResponseEntity<?> createYear(
      @PathVariable UUID schoolId,
      @Valid @RequestBody CreateAcademicYearRequest req,
      Authentication auth
   ) {
      log.info("Creating academic year - User: {}, SchoolId: {}, Year: {}", 
               auth.getName(), schoolId, req.yearLabel());
      var result = service.createAcademicYear(schoolId, req.yearLabel());
      log.info("Academic year created successfully - SchoolId: {}, Year: {}", 
               schoolId, req.yearLabel());
      return ResponseEntity.ok(result);
   }
}
