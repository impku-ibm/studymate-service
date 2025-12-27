package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.schoolmodule.dtos.CreateAcademicYearRequest;
import com.portal.studymate.schoolmodule.model.AcademicYearEntity;
import com.portal.studymate.schoolmodule.service.AcademicYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school/academic-years")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AcademicYearController {
   private final AcademicYearService service;
   private final JwtContextService jwtContextService;

   @GetMapping
   public List<AcademicYearEntity> getAll() {
      String schoolId = jwtContextService.getSchoolId();
      log.info("GET academic years API called for schoolId={}", schoolId);
      return service.getAll(schoolId);
   }

   @GetMapping("/active")
   public AcademicYearEntity getActive() {
      String schoolId = jwtContextService.getSchoolId();
      log.info("GET active academic year for schoolId={}", schoolId);
      return service.getActive(schoolId);
   }

   @PostMapping("/setup")
   public ResponseEntity<String> create(
      @Valid @RequestBody CreateAcademicYearRequest request
   ) {
      String schoolId = jwtContextService.getSchoolId();
      log.info(
         "POST academic year API called for schoolId={}, year={}",
         schoolId, request.getYear()
      );
      service.addAcademicYear(schoolId, request);
      return ResponseEntity.ok("Academic year added successfully");
   }
}
