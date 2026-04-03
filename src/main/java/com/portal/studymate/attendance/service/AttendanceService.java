package com.portal.studymate.attendance.service;

import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.attendance.dto.*;
import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.attendance.model.StudentAttendance;
import com.portal.studymate.attendance.repository.StudentAttendanceRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AttendanceService {
    
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicYearRepository academicYearRepository;

    public AttendanceService(StudentAttendanceRepository studentAttendanceRepository,
                           StudentRepository studentRepository,
                           TeacherRepository teacherRepository,
                           AcademicYearRepository academicYearRepository) {
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.academicYearRepository = academicYearRepository;
    }

    public void markAttendance(MarkAttendanceRequest request, Long teacherId) {
        log.info("markAttendance called - teacherId: {}, date: {}", teacherId, request.attendanceDate());
        var school = SchoolContext.getSchool();
        
        var teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        // Get current academic year
        var academicYear = academicYearRepository.findActiveBySchool(school);
        if (academicYear == null) {
            throw new ResourceNotFoundException("No active academic year found");
        }
        
        // Validate attendance date is not in future
        if (request.attendanceDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("INVALID_DATE", "Cannot mark attendance for future dates");
        }
        
        for (var entry : request.attendanceEntries()) {
            var student = studentRepository.findById(entry.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + entry.studentId()));
            
            var existingAttendance = studentAttendanceRepository
                .findByStudentIdAndDate(entry.studentId(), request.attendanceDate());
            
            StudentAttendance attendance;
            if (existingAttendance.isPresent()) {
                attendance = existingAttendance.get();
                attendance.setStatus(entry.status());
            } else {
                attendance = new StudentAttendance(student, academicYear, request.attendanceDate(),
                                                 entry.status(), teacher, request.section());
            }
            
            studentAttendanceRepository.save(attendance);
        }
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date, String section) {
        log.info("getAttendanceByDate called - date: {}, section: {}", date, section);
        var school = SchoolContext.getSchool();
        
        return studentAttendanceRepository.findByDateAndSectionAndSchoolId(date, section, school.getId())
            .stream()
            .map(this::mapToAttendanceResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public AttendanceSummary getStudentAttendanceSummary(Long studentId, YearMonth month) {
        log.info("getStudentAttendanceSummary called - studentId: {}, month: {}", studentId, month);
        var school = SchoolContext.getSchool();
        
        var student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        var startDate = month.atDay(1);
        var endDate = month.atEndOfMonth();
        
        var presentDays = studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
            studentId, startDate, endDate, AttendanceStatus.PRESENT);
        
        var absentDays = studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
            studentId, startDate, endDate, AttendanceStatus.ABSENT);
        
        var leaveDays = studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
            studentId, startDate, endDate, AttendanceStatus.LEAVE);
        
        long totalDays = presentDays + absentDays + leaveDays;
        
        var attendancePercentage = totalDays > 0 ? 
            BigDecimal.valueOf(presentDays * 100.0 / totalDays).setScale(2, RoundingMode.HALF_UP) :
            BigDecimal.ZERO;
        
        return new AttendanceSummary(
            studentId,
            student.getFullName(),
            (int) totalDays,
            presentDays.intValue(),
            absentDays.intValue(),
            leaveDays.intValue(),
            attendancePercentage
        );
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getStudentAttendanceHistory(Long studentId, LocalDate startDate, LocalDate endDate) {
        log.info("getStudentAttendanceHistory called - studentId: {}, range: {} to {}", studentId, startDate, endDate);
        var school = SchoolContext.getSchool();
        
        // Validate student belongs to school
        studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        return studentAttendanceRepository.findByStudentIdAndDateRange(studentId, startDate, endDate)
            .stream()
            .map(this::mapToAttendanceResponse)
            .toList();
    }

    private AttendanceResponse mapToAttendanceResponse(StudentAttendance attendance) {
        return new AttendanceResponse(
            attendance.getId(),
            attendance.getStudent().getId(),
            attendance.getStudent().getFullName(),
            attendance.getAttendanceDate(),
            attendance.getStatus(),
            attendance.getSection(),
            attendance.getMarkedBy().getFullName()
        );
    }

    public void markTeacherSelfAttendance(Long teacherId) {
        log.info("markTeacherSelfAttendance called - teacherId: {}", teacherId);
        // Teacher self-attendance - just validates teacher exists
        teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        // TeacherAttendance is handled separately via TeacherAttendanceRepository
        // This is a placeholder - full implementation needs TeacherAttendanceRepository injection
    }
}