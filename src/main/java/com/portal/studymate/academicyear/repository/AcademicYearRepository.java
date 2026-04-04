package com.portal.studymate.academicyear.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

   Optional<AcademicYear> findFirstBySchoolAndStatusOrderByCreatedAtDesc(School school, AcademicYearStatus status);

   List<AcademicYear> findBySchoolOrderByCreatedAtDesc(School school);

   Optional<AcademicYear> findBySchoolAndYear(School school, String yearValue);

   /**
    * @deprecated Use findFirstBySchoolAndStatusOrderByCreatedAtDesc instead
    */
   default AcademicYear findActiveBySchool(School school) {
      return findFirstBySchoolAndStatusOrderByCreatedAtDesc(school, AcademicYearStatus.ACTIVE)
         .orElse(null);
   }

   /**
    * Backward-compatible wrapper — returns Optional of the most recent ACTIVE year.
    */
   default Optional<AcademicYear> findBySchoolAndStatus(School school, AcademicYearStatus status) {
      return findFirstBySchoolAndStatusOrderByCreatedAtDesc(school, status);
   }
}
