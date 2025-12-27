package com.portal.studymate.schoolmodule.repository;

import com.portal.studymate.schoolmodule.model.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentEnrollmentRepository
   extends JpaRepository<StudentEnrollment, Long> {

   boolean existsByStudentIdAndAcademicYearId(Long studentId, Long yearId);

   @Query("""
     SELECT COALESCE(MAX(e.rollNumber),0)+1
     FROM StudentEnrollment e
     WHERE e.schoolClass.id = :classId
       AND e.section.id = :sectionId
       AND e.academicYear.id = :yearId
   """)
   int nextRollNumber(Long classId, Long sectionId, Long yearId);

   List<StudentEnrollment> findBySchoolIdAndAcademicYearId(
      String schoolId, Long yearId);
}

