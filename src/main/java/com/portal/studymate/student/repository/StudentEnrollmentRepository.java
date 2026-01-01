package com.portal.studymate.student.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.student.model.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentEnrollmentRepository
   extends JpaRepository<StudentEnrollment, Long> {

   List<StudentEnrollment> findByAcademicYear(AcademicYear year);
}