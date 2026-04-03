package com.portal.studymate.accounts.repository;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.model.StudentFeeConfiguration;
import com.portal.studymate.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing student fee configurations.
 * Provides queries to fetch which fee types are applicable for each student.
 */
public interface StudentFeeConfigurationRepository extends JpaRepository<StudentFeeConfiguration, Long> {

   /**
    * Find all active fee configurations for a student in a specific academic year.
    * Used during fee generation to determine which fees to create for the student.
    * 
    * @param student The student
    * @param academicYear The academic year
    * @return List of active fee configurations
    */
   @Query("SELECT sfc FROM StudentFeeConfiguration sfc WHERE sfc.student = :student AND sfc.academicYear = :academicYear AND sfc.active = true")
   List<StudentFeeConfiguration> findByStudentAndAcademicYearAndActiveTrue(
      @Param("student") Student student,
      @Param("academicYear") AcademicYear academicYear
   );

   /**
    * Find all fee configurations (active and inactive) for a student in an academic year.
    * Used in fee management UI to show complete fee history.
    * 
    * @param student The student
    * @param academicYear The academic year
    * @return List of all fee configurations
    */
   List<StudentFeeConfiguration> findByStudentAndAcademicYear(Student student, AcademicYear academicYear);

   /**
    * Find a specific fee configuration for a student.
    * Used to check if a fee type is already configured before adding.
    * 
    * @param student The student
    * @param academicYear The academic year
    * @param feeType The fee type
    * @return Optional fee configuration
    */
   Optional<StudentFeeConfiguration> findByStudentAndAcademicYearAndFeeType(
      Student student,
      AcademicYear academicYear,
      FeeType feeType
   );

   /**
    * Count active fee types for a student.
    * Used for validation and reporting.
    * 
    * @param student The student
    * @param academicYear The academic year
    * @return Count of active fee types
    */
   @Query("SELECT COUNT(sfc) FROM StudentFeeConfiguration sfc WHERE sfc.student = :student AND sfc.academicYear = :academicYear AND sfc.active = true")
   Long countActiveByStudentAndAcademicYear(
      @Param("student") Student student,
      @Param("academicYear") AcademicYear academicYear
   );

   @Query("SELECT COUNT(sfc) > 0 FROM StudentFeeConfiguration sfc WHERE sfc.student = :student AND sfc.feeType = :feeType AND sfc.active = true")
   boolean existsByStudentAndFeeTypeAndActiveTrue(@Param("student") Student student, @Param("feeType") FeeType feeType);
}
