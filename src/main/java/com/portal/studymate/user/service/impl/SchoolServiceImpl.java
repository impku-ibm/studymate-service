package com.portal.studymate.user.service.impl;

import com.portal.studymate.common.exception.SchoolNotFoundException;
import com.portal.studymate.user.model.AcademicYear;
import com.portal.studymate.user.model.School;
import com.portal.studymate.user.repository.AcademicYearRepository;
import com.portal.studymate.user.repository.SchoolRepository;
import com.portal.studymate.user.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolServiceImpl implements SchoolService {
   private final SchoolRepository schoolRepository;
   private final AcademicYearRepository academicYearRepository;
   @Override
   public School createSchool(School school) {
      log.info("Creating school - Name: {}, Board: {}", school.getName(), school.getBoard());
      try {
         School savedSchool = schoolRepository.save(school);
         log.info("School created successfully - ID: {}, Name: {}", savedSchool.getId(), savedSchool.getName());
         return savedSchool;
      } catch (Exception e) {
         log.error("Failed to create school - Name: {}, Error: {}", school.getName(), e.getMessage(), e);
         throw e;
      }
   }

   @Override
   public AcademicYear createAcademicYear(UUID schoolId, String yearLabel) {
      log.info("Creating academic year - SchoolId: {}, Year: {}", schoolId, yearLabel);
      try {
         // Validate school exists
         if (!schoolRepository.existsById(schoolId)) {
            log.error("School not found - SchoolId: {}", schoolId);
            throw new SchoolNotFoundException("School not found with ID: " + schoolId);
         }
         
         AcademicYear academicYear = AcademicYear.builder()
                                                 .schoolId(schoolId)
                                                 .yearLabel(yearLabel)
                                                 .active(true)
                                                 .build();
         
         AcademicYear savedYear = academicYearRepository.save(academicYear);
         log.info("Academic year created successfully - ID: {}, SchoolId: {}, Year: {}", 
                  savedYear.getId(), schoolId, yearLabel);
         return savedYear;
      } catch (Exception e) {
         log.error("Failed to create academic year - SchoolId: {}, Year: {}, Error: {}", 
                   schoolId, yearLabel, e.getMessage(), e);
         throw e;
      }
   }
}
