package com.portal.studymate.exam.repository;

import com.portal.studymate.exam.model.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    
    @Query("SELECT es FROM ExamSchedule es WHERE es.exam.school.id = :schoolId AND es.id = :id")
    Optional<ExamSchedule> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT es FROM ExamSchedule es WHERE es.exam.id = :examId")
    List<ExamSchedule> findByExamId(@Param("examId") Long examId);
    
    @Query("SELECT es FROM ExamSchedule es WHERE es.exam.school.id = :schoolId AND es.schoolClass.id = :classId AND es.section = :section")
    List<ExamSchedule> findBySchoolIdAndClassIdAndSection(@Param("schoolId") Long schoolId,
                                                         @Param("classId") Long classId,
                                                         @Param("section") String section);
}