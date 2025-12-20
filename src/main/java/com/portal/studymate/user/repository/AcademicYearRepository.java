package com.portal.studymate.user.repository;

import com.portal.studymate.user.model.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, UUID> {
   List<AcademicYear> findBySchoolId(UUID schoolid);

   AcademicYear findBySchoolIdAndActiveTrue(UUID schoolid);
}
