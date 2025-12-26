package com.portal.studymate.user.service.impl;

import com.portal.studymate.common.exception.AcademicYearAlreadyExistsException;
import com.portal.studymate.common.exception.AcademicYearNotFoundException;
import com.portal.studymate.common.util.AcademicYearStatus;
import com.portal.studymate.user.dtos.CreateAcademicYearRequest;
import com.portal.studymate.user.model.AcademicYearEntity;
import com.portal.studymate.user.repository.AcademicYearRepository;
import com.portal.studymate.user.service.AcademicYearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcademicYearServiceImpl implements AcademicYearService {

   private final AcademicYearRepository repository;

   @Override
   public List<AcademicYearEntity> getAll(String schoolId) {
      log.info("Fetching academic years for schoolId={}", schoolId);
      return repository.findBySchoolIdOrderByStartDateDesc(schoolId);
   }

   @Override
   public AcademicYearEntity getActive(String schoolId) {
      log.info("Fetching active academic year for schoolId={}", schoolId);
      return repository.findBySchoolIdAndStatus(
                          schoolId, AcademicYearStatus.ACTIVE
                       )
                       .orElseThrow(() -> {
                          log.warn("No active academic year found for schoolId={}", schoolId);
                          return new AcademicYearNotFoundException();
                       });
   }

   @Override
   @Transactional
   public void addAcademicYear(String schoolId,
                               CreateAcademicYearRequest request) {
      log.info(
         "Adding academic year={} for schoolId={}",
         request.getYear(), schoolId
      );
      // Prevent duplicates
      boolean exists = repository.findBySchoolIdOrderByStartDateDesc(schoolId)
                                 .stream()
                                 .anyMatch(y -> y.getYearLabel().equals(request.getYear()));

      if (exists) {
         log.warn(
            "Academic year already exists: {} for schoolId={}",
            request.getYear(), schoolId
         );
         throw new AcademicYearAlreadyExistsException(request.getYear());
      }
      // 1. Mark existing ACTIVE year as COMPLETED
      repository.findBySchoolIdAndStatus(schoolId, AcademicYearStatus.ACTIVE)
                .ifPresent(year -> {
                   log.info(
                      "Marking academic year={} as COMPLETED",
                      year.getYearLabel()
                   );
                   year.setStatus(AcademicYearStatus.COMPLETED);
                   repository.save(year);
                });

      // 2. Create new ACTIVE year
      AcademicYearEntity year = new AcademicYearEntity();
      year.setSchoolId(schoolId);
      year.setYearLabel(request.getYear());
      year.setStartDate(request.getStartDate());
      year.setEndDate(request.getEndDate());
      year.setStatus(AcademicYearStatus.ACTIVE);

      repository.save(year);
      log.info(
         "Academic year={} created successfully for schoolId={}",
         request.getYear(), schoolId
      );
   }
}
