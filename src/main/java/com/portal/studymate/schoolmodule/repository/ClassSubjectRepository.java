package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.ClassSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {

   boolean existsByAcademicYearIdAndClassIdAndSectionIdAndSubjectId(
      Long academicYearId,
      Long classId,
      Long sectionId,
      Long subjectId
   );

   List<ClassSubject> findByAcademicYearIdAndClassIdAndSectionId(
      Long academicYearId,
      Long classId,
      Long sectionId
   );

   boolean existsByClassIdAndSectionIdAndSubjectId(
      Long classId,
      Long sectionId,
      Long subjectId
   );
}
