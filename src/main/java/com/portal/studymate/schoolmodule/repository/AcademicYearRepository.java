package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.common.util.AcademicYearStatus;
import com.portal.studymate.schoolmodule.model.AcademicYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, Long> {
   List<AcademicYearEntity> findBySchoolIdOrderByStartDateDesc(String schoolId);

   Optional<AcademicYearEntity> findBySchoolIdAndStatus(
      String schoolId,
      AcademicYearStatus status
   );
   Optional<AcademicYearEntity> findByIdAndSchoolId(Long id, String schoolId);

   @Query("""
 SELECT ay FROM AcademicYear ay
 WHERE ay.schoolId = :schoolId
   AND ay.startYear = :startYear
""")
   Optional<AcademicYearEntity> findNextYear(String schoolId, int startYear);
}
