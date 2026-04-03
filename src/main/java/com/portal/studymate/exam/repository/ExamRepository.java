package com.portal.studymate.exam.repository;

import com.portal.studymate.exam.enums.ExamType;
import com.portal.studymate.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    @Query("SELECT e FROM Exam e WHERE e.school.id = :schoolId")
    List<Exam> findBySchoolId(@Param("schoolId") Long schoolId);
    
    @Query("SELECT e FROM Exam e WHERE e.school.id = :schoolId AND e.id = :id")
    Optional<Exam> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT e FROM Exam e WHERE e.school.id = :schoolId AND e.academicYear.id = :academicYearId")
    List<Exam> findBySchoolIdAndAcademicYearId(@Param("schoolId") Long schoolId, 
                                               @Param("academicYearId") Long academicYearId);
    
    @Query("SELECT COUNT(e) > 0 FROM Exam e WHERE e.school.id = :schoolId AND e.academicYear.id = :academicYearId AND e.examType = :examType")
    boolean existsBySchoolIdAndAcademicYearIdAndExamType(@Param("schoolId") Long schoolId,
                                                        @Param("academicYearId") Long academicYearId,
                                                        @Param("examType") ExamType examType);
}