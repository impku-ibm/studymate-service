package com.portal.studymate.academicyear.service.impl;

import com.portal.studymate.academicyear.dto.AcademicYearResponse;
import com.portal.studymate.academicyear.dto.CreateAcademicYearRequest;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.academicyear.service.AcademicYearService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AcademicYearServiceImpl implements AcademicYearService {

   private final AcademicYearRepository academicYearRepository;

   // =========================
   // CREATE ACADEMIC YEAR
   // =========================
   @Override
   public AcademicYearResponse createAcademicYear(CreateAcademicYearRequest request) {
      log.info("createAcademicYear called - startYear: {}", request.getStartYear());

      School school = SchoolContext.getSchool();

      if (school == null) {
         throw new ResourceNotFoundException(
            "SCHOOL_NOT_FOUND"
         );
      }

      int startYear = request.getStartYear();
      int endYear = startYear + 1;
      String yearValue = startYear + "-" + endYear;

      // Can't create a past academic year
      validateNotPastYear(yearValue);

      // Prevent duplicate academic year
      academicYearRepository.findBySchoolAndYear(school, yearValue)
                            .ifPresent(existing -> {
                               throw new ConflictException(
                                  "ACADEMIC_YEAR_EXISTS",
                                  "Academic year already exists"
                               );
                            });

      // Deactivate ALL currently active years for this school
      List<AcademicYear> allYears = academicYearRepository.findBySchoolOrderByCreatedAtDesc(school);
      for (AcademicYear ay : allYears) {
         if (ay.getStatus() == AcademicYearStatus.ACTIVE) {
            ay.setStatus(AcademicYearStatus.COMPLETED);
            ay.setActive(false);
            academicYearRepository.save(ay);
         }
      }

      AcademicYear academicYear = AcademicYear.builder()
                                              .school(school)
                                              .year(yearValue)
                                              .status(AcademicYearStatus.ACTIVE)
                                              .active(true)
                                              .build();

      AcademicYear saved = academicYearRepository.save(academicYear);

      return toResponse(saved);
   }

   // =========================
   // GET ACTIVE ACADEMIC YEAR
   // =========================
   @Override
   @Transactional(readOnly = true)
   public AcademicYearResponse getActiveAcademicYear() {
      log.info("getActiveAcademicYear called");

      School school = SchoolContext.getSchool();

      return academicYearRepository
                .findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)
                .map(this::toResponse)
                .orElseThrow(() ->
                                new ResourceNotFoundException(
                                   "ACTIVE_ACADEMIC_YEAR_NOT_FOUND"
                                )
                );
   }

   // =========================
   // LIST ALL ACADEMIC YEARS
   // =========================
   @Override
   @Transactional(readOnly = true)
   public List<AcademicYearResponse> getAllAcademicYears() {
      log.info("getAllAcademicYears called");

      School school = SchoolContext.getSchool();

      return academicYearRepository
                .findBySchoolOrderByCreatedAtDesc(school)
                .stream()
                .map(this::toResponse)
                .toList();
   }

   // =========================
   // MAPPER
   // =========================
   private AcademicYearResponse toResponse(AcademicYear ay) {
      return AcademicYearResponse.builder()
                                 .id(ay.getId())
                                 .year(ay.getYear())
                                 .status(ay.getStatus().name())
                                 .build();
   }

   /**
    * Validates that the academic year is not in the past.
    * Year format: "2025-2026". The end year (2026) must be >= current calendar year.
    * Indian schools: session runs April to March, so "2025-2026" is valid until March 2026.
    * We allow a 3-month grace (until June of end year) before considering it past.
    */
   private void validateNotPastYear(String yearValue) {
      try {
         String[] parts = yearValue.split("-");
         int endYear = Integer.parseInt(parts[1]);
         LocalDate now = LocalDate.now();
         // Session "2025-2026" ends in March 2026, grace until June 2026
         LocalDate cutoff = LocalDate.of(endYear, 6, 30);
         if (now.isAfter(cutoff)) {
            throw new com.portal.studymate.common.exception.BadRequestException(
               "PAST_ACADEMIC_YEAR",
               "Cannot activate academic year " + yearValue + " — it has already ended."
            );
         }
      } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
         // Can't parse year format, skip validation
         log.warn("Could not parse academic year format: {}", yearValue);
      }
   }

   // =========================
   // ACTIVATE ACADEMIC YEAR
   // =========================
   @Override
   public AcademicYearResponse activateAcademicYear(Long id) {
      log.info("activateAcademicYear called - id: {}", id);
      School school = SchoolContext.getSchool();

      AcademicYear target = academicYearRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("ACADEMIC_YEAR_NOT_FOUND"));

      // Validate: can't activate a past academic year
      // Year format is "2025-2026" — the end year must be >= current year
      validateNotPastYear(target.getYear());

      // Deactivate ALL currently active years for this school
      List<AcademicYear> allYears = academicYearRepository.findBySchoolOrderByCreatedAtDesc(school);
      for (AcademicYear ay : allYears) {
         if (ay.getStatus() == AcademicYearStatus.ACTIVE && !ay.getId().equals(id)) {
            ay.setStatus(AcademicYearStatus.COMPLETED);
            ay.setActive(false);
            academicYearRepository.save(ay);
            log.info("Deactivated academic year: {}", ay.getYear());
         }
      }

      // Activate the target year
      target.setStatus(AcademicYearStatus.ACTIVE);
      target.setActive(true);
      AcademicYear saved = academicYearRepository.save(target);

      return toResponse(saved);
   }
}
