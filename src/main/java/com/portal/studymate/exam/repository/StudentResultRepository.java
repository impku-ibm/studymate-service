package com.portal.studymate.exam.repository;

import com.portal.studymate.exam.model.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {
    
    @Query("SELECT sr FROM StudentResult sr WHERE sr.exam.school.id = :schoolId AND sr.id = :id")
    Optional<StudentResult> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT sr FROM StudentResult sr WHERE sr.exam.id = :examId ORDER BY sr.percentage DESC")
    List<StudentResult> findByExamIdOrderByPercentageDesc(@Param("examId") Long examId);
    
    @Query("SELECT sr FROM StudentResult sr WHERE sr.student.id = :studentId AND sr.exam.school.id = :schoolId")
    List<StudentResult> findByStudentIdAndSchoolId(@Param("studentId") Long studentId, @Param("schoolId") Long schoolId);
    
    @Query("SELECT sr FROM StudentResult sr WHERE sr.exam.id = :examId AND sr.student.id = :studentId")
    Optional<StudentResult> findByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);
}