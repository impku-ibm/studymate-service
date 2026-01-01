package com.portal.studymate.classmanagement.repository;

import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassSectionTemplateRepository
   extends JpaRepository<ClassSectionTemplate, Long> {

   List<ClassSectionTemplate> findBySchoolClass(SchoolClass schoolClass);

   Optional<ClassSectionTemplate> findBySchoolClassAndName(
      SchoolClass schoolClass,
      String name
   );
}

