package com.portal.studymate.user.repository;

import com.portal.studymate.common.util.AcademicYearStatus;
import com.portal.studymate.user.model.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, Long> {
   List<AcademicYearEntity> findBySchoolIdOrderByStartDateDesc(String schoolId);

   Optional<AcademicYearEntity> findBySchoolIdAndStatus(
      String schoolId,
      AcademicYearStatus status
   );
}
