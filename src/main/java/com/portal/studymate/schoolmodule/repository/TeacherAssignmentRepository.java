package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment,Long> {
   List<TeacherAssignment> findBySchoolIdAndAcademicYearId(
      String schoolId, Long academicYearId);

   List<TeacherAssignment> findByTeacherId(Long teacherId);

   boolean existsByTeacherIdAndClassIdAndSectionAndSubjectIdAndAcademicYearId(
      Long teacherId,
      Long classId,
      String section,
      Long subjectId,
      Long academicYearId
   );
}
