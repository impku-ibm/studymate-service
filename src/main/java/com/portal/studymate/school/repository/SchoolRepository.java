package com.portal.studymate.school.repository;

import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

   Optional<School> findByActiveTrue();

   boolean existsBySchoolCode(String schoolCode);
   Optional<School> findBySchoolCodeAndActiveTrue(String schoolCode);
}

