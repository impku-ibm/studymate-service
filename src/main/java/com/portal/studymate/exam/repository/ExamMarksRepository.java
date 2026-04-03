package com.portal.studymate.exam.repository;

import com.portal.studymate.exam.model.ExamMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamMarksRepository extends JpaRepository<ExamMarks, Long> {
    
    @Query("SELECT em FROM ExamMarks em WHERE em.examSchedule.exam.school.id = :schoolId AND em.id = :id")
    Optional<ExamMarks> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT em FROM ExamMarks em WHERE em.examSchedule.id = :examScheduleId")
    List<ExamMarks> findByExamScheduleId(@Param("examScheduleId") Long examScheduleId);
    
    @Query("SELECT em FROM ExamMarks em WHERE em.examSchedule.id = :examScheduleId AND em.student.id = :studentId")
    Optional<ExamMarks> findByExamScheduleIdAndStudentId(@Param("examScheduleId") Long examScheduleId,
                                                        @Param("studentId") Long studentId);
    
    @Query("SELECT em FROM ExamMarks em WHERE em.examSchedule.exam.id = :examId AND em.student.id = :studentId")
    List<ExamMarks> findByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);
}