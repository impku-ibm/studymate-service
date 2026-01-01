package com.portal.studymate.teacherassignment.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.teacherassignment.model.TeacherAssignment;
import com.portal.studymate.teachermgmt.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherAssignmentRepository
   extends JpaRepository<TeacherAssignment, Long> {

   Optional<TeacherAssignment>
   findByAcademicYearAndSectionIdAndSubjectId(
      AcademicYear academicYear,
      Long sectionId,
      Long subjectId
   );

   List<TeacherAssignment>
   findByTeacherAndAcademicYear(
      Teacher teacher,
      AcademicYear academicYear
   );

   List<TeacherAssignment>
   findByAcademicYearAndSectionId(
      AcademicYear academicYear,
      Long sectionId
   );
}

