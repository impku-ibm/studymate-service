package com.portal.studymate.academicyear.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

   Optional<AcademicYear> findBySchoolAndStatus(School school, AcademicYearStatus status);

   List<AcademicYear> findBySchoolOrderByCreatedAtDesc(School school);

   Optional<Object> findBySchoolAndYear(School school, String yearValue);

   AcademicYear findActiveBySchool(School school);
}

