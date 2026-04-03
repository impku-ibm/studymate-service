package com.portal.studymate.student.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.model.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentEnrollmentRepository
   extends JpaRepository<StudentEnrollment, Long> {

   List<StudentEnrollment> findByAcademicYear(AcademicYear year);
   List<StudentEnrollment> findByAcademicYearAndSchoolClass(AcademicYear academicYear, SchoolClass schoolClass);

   @Query("SELECT se FROM StudentEnrollment se WHERE se.student = :student AND se.status = 'ACTIVE' ORDER BY se.id DESC")
   Optional<StudentEnrollment> findActiveByStudent(@Param("student") Student student);
}