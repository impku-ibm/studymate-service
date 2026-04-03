package com.portal.studymate.attendance.repository;

import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.attendance.model.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long> {
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.student.school.id = :schoolId AND sa.id = :id")
    Optional<StudentAttendance> findByIdAndSchoolId(@Param("id") Long id, @Param("schoolId") Long schoolId);
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.student.id = :studentId AND sa.attendanceDate = :date")
    Optional<StudentAttendance> findByStudentIdAndDate(@Param("studentId") Long studentId, 
                                                      @Param("date") LocalDate date);
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.student.id = :studentId " +
           "AND sa.attendanceDate BETWEEN :startDate AND :endDate ORDER BY sa.attendanceDate")
    List<StudentAttendance> findByStudentIdAndDateRange(@Param("studentId") Long studentId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.attendanceDate = :date " +
           "AND sa.section = :section AND sa.student.school.id = :schoolId")
    List<StudentAttendance> findByDateAndSectionAndSchoolId(@Param("date") LocalDate date,
                                                           @Param("section") String section,
                                                           @Param("schoolId") Long schoolId);
    
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.student.id = :studentId " +
           "AND sa.attendanceDate BETWEEN :startDate AND :endDate AND sa.status = :status")
    Long countByStudentIdAndDateRangeAndStatus(@Param("studentId") Long studentId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("status") AttendanceStatus status);
}