package com.portal.studymate.school.service.impl;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.dtos.CreateSchoolRequest;
import com.portal.studymate.school.dtos.SchoolResponse;
import com.portal.studymate.school.dtos.UpdateSchoolRequest;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.repository.SchoolRepository;
import com.portal.studymate.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolServiceImpl implements SchoolService {

   private final SchoolRepository schoolRepository;

   // =========================
   // GET CURRENT SCHOOL
   // =========================
   @Override
   @Transactional(readOnly = true)
   public SchoolResponse getCurrentSchool() {

      School school = SchoolContext.getSchool();

      if (school == null) {
         throw new ResourceNotFoundException(
            "SCHOOL_NOT_FOUND",
            "School context not available"
         );
      }

      return toResponse(school);
   }


   // =========================
   // CREATE SCHOOL (BOOTSTRAP)
   // =========================
   @Override
   public SchoolResponse createSchool(CreateSchoolRequest request) {

      // Only one school allowed
      schoolRepository.findByActiveTrue().ifPresent(existing -> {
         throw new ConflictException(
            "SCHOOL_ALREADY_EXISTS",
            "School is already configured"
         );
      });

      if (schoolRepository.existsBySchoolCode(request.getSchoolCode())) {
         throw new ConflictException(
            "DUPLICATE_SCHOOL_CODE",
            "School code already exists"
         );
      }

      School school = School.builder()
                            .name(request.getName())
                            .board(request.getBoard())
                            .address(request.getAddress())
                            .city(request.getCity())
                            .state(request.getState())
                            .academicStartMonth(request.getAcademicStartMonth())
                            .schoolCode(request.getSchoolCode())
                            .active(true)
                            .build();

      School saved = schoolRepository.save(school);

      return toResponse(saved);
   }

   // =========================
   // UPDATE SCHOOL
   // =========================
   @Override
   public SchoolResponse updateSchool(UpdateSchoolRequest request) {

      School existing = SchoolContext.getSchool();

      if (existing == null) {
         throw new ResourceNotFoundException(
            "SCHOOL_NOT_FOUND",
            "School context not available"
         );
      }

      existing.setName(request.getName());
      existing.setBoard(request.getBoard());
      existing.setAddress(request.getAddress());
      existing.setCity(request.getCity());
      existing.setState(request.getState());
      existing.setAcademicStartMonth(request.getAcademicStartMonth());

      School updated = schoolRepository.save(existing);

      return toResponse(updated);
   }

   // =========================
   // MAPPER (SINGLE SOURCE)
   // =========================
   private SchoolResponse toResponse(School school) {
      return SchoolResponse.builder()
                           .id(school.getId())
                           .name(school.getName())
                           .board(school.getBoard())
                           .city(school.getCity())
                           .state(school.getState())
                           .academicStartMonth(school.getAcademicStartMonth())
                           .schoolCode(school.getSchoolCode())
                           .build();
   }
}

