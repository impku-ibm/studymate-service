package com.portal.studymate.classsubject.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classsubject.model.ClassSubject;
import com.portal.studymate.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassSubjectRepository
   extends JpaRepository<ClassSubject, Long> {

   List<ClassSubject> findByAcademicYearAndSchoolClass(
      AcademicYear academicYear,
      SchoolClass schoolClass
   );

   boolean existsByAcademicYearAndSchoolClassAndSubject(
      AcademicYear academicYear,
      SchoolClass schoolClass,
      Subject subject
   );

   List<ClassSubject> findByAcademicYear(AcademicYear academicYear);
}

