package com.portal.studymate.academicyear.controller;

import com.portal.studymate.academicyear.dto.AcademicYearResponse;
import com.portal.studymate.academicyear.dto.CreateAcademicYearRequest;
import com.portal.studymate.academicyear.service.AcademicYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/academic-years")
@RequiredArgsConstructor
@Slf4j
public class AcademicYearController {
   private final AcademicYearService service;

   @PostMapping
   public ResponseEntity<AcademicYearResponse> create(
      @Valid @RequestBody CreateAcademicYearRequest request) {

      return ResponseEntity.ok(service.createAcademicYear(request));
   }

   @GetMapping
   public ResponseEntity<List<AcademicYearResponse>> list() {
      //calling this
      log.info("Getting all academic years");
      return ResponseEntity.ok(service.getAllAcademicYears());
   }

   @GetMapping("/active")
   public ResponseEntity<AcademicYearResponse> getActiveAcademicYear(){
      log.info("Getting active academic year");
      return ResponseEntity.ok(service.getActiveAcademicYear());
   }
}
