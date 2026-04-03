package com.portal.studymate.accounts.repository;

import com.portal.studymate.accounts.model.FeeStructure;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.school.model.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {

   @Query("SELECT fs FROM FeeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear AND fs.active = true")
   Page<FeeStructure> findBySchoolAndAcademicYear(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear,
      Pageable pageable
   );

   @Query("SELECT fs FROM FeeStructure fs WHERE fs.school = :school AND fs.academicYear = :academicYear AND fs.schoolClass = :schoolClass AND fs.active = true")
   List<FeeStructure> findBySchoolAndAcademicYearAndClass(
      @Param("school") School school,
      @Param("academicYear") AcademicYear academicYear,
      @Param("schoolClass") SchoolClass schoolClass
   );

   Optional<FeeStructure> findBySchoolAndAcademicYearAndSchoolClassAndFeeType(
      School school,
      AcademicYear academicYear,
      SchoolClass schoolClass,
      FeeType feeType
   );

   List<FeeStructure> findBySchoolAndFeeType(School school, FeeType feeType);
}