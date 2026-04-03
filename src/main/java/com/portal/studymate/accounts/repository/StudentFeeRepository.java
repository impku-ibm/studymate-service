package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.StudentFee;
import com.portal.studymate.accounts.enums.StudentFeeStatus;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.school.model.School;
import com.portal.studymate.academicyear.model.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StudentFeeRepository extends JpaRepository<StudentFee, Long> {

   @Query("SELECT sf FROM StudentFee sf WHERE sf.student = :student")
   Page<StudentFee> findByStudent(@Param("student") Student student, Pageable pageable);

   @Query("SELECT sf FROM StudentFee sf JOIN sf.feeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear AND sf.status = :status")
   List<StudentFee> findBySchoolAndAcademicYearAndStatus(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear,
      @Param("status") StudentFeeStatus status
   );

   @Query("SELECT COALESCE(SUM(sf.paidAmount), 0) FROM StudentFee sf JOIN sf.feeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear")
   BigDecimal getTotalCollectionBySchoolAndAcademicYear(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear
   );

   @Query("SELECT COALESCE(SUM(sf.totalAmount - sf.paidAmount), 0) FROM StudentFee sf JOIN sf.feeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear AND sf.status != 'PAID'")
   BigDecimal getTotalPendingBySchoolAndAcademicYear(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear
   );

   @Query("SELECT COUNT(DISTINCT sf.student) FROM StudentFee sf JOIN sf.feeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear")
   Long getTotalStudentsBySchoolAndAcademicYear(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear
   );

   @Query("SELECT sf FROM StudentFee sf WHERE sf.student = :student AND sf.feeStructure = :feeStructure")
   java.util.Optional<StudentFee> findByStudentAndFeeStructure(
      @Param("student") Student student,
      @Param("feeStructure") com.portal.studymate.accounts.model.FeeStructure feeStructure
   );
}