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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

      School school = SchoolContext.getSchool();

      if (school == null) {
         throw new ResourceNotFoundException(
            "SCHOOL_NOT_FOUND",
            "School context not available"
         );
      }

      int startYear = request.getStartYear();
      int endYear = startYear + 1;
      String yearValue = startYear + "-" + endYear;

      // Prevent duplicate academic year
      academicYearRepository.findBySchoolAndYear(school, yearValue)
                            .ifPresent(existing -> {
                               throw new ConflictException(
                                  "ACADEMIC_YEAR_EXISTS",
                                  "Academic year already exists"
                               );
                            });

      // Mark current ACTIVE year as COMPLETED (if any)
      academicYearRepository.findBySchoolAndStatus(
                               school, AcademicYearStatus.ACTIVE)
                            .ifPresent(active -> {
                               active.setStatus(AcademicYearStatus.COMPLETED);
                               academicYearRepository.save(active);
                            });

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

      School school = SchoolContext.getSchool();

      return academicYearRepository
                .findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE)
                .map(this::toResponse)
                .orElseThrow(() ->
                                new ResourceNotFoundException(
                                   "ACTIVE_ACADEMIC_YEAR_NOT_FOUND",
                                   "No active academic year configured"
                                )
                );
   }

   // =========================
   // LIST ALL ACADEMIC YEARS
   // =========================
   @Override
   @Transactional(readOnly = true)
   public List<AcademicYearResponse> getAllAcademicYears() {

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
}
