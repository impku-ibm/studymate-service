package com.portal.studymate.classmanagement.repository;

import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.school.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

   List<SchoolClass> findBySchool(School school);

   Optional<SchoolClass> findBySchoolAndName(School school, String name);

   long countBySchool(School school);
}

