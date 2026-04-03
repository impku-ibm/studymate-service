package com.portal.studymate.attendance.service;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.attendance.dto.AttendanceSummary;
import com.portal.studymate.attendance.dto.MarkAttendanceRequest;
import com.portal.studymate.attendance.enums.AttendanceStatus;
import com.portal.studymate.attendance.repository.StudentAttendanceRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock private StudentAttendanceRepository studentAttendanceRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private AcademicYearRepository academicYearRepository;

    private AttendanceService attendanceService;
    private School school;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(studentAttendanceRepository,
                studentRepository, teacherRepository, academicYearRepository);
        school = new School();
        school.setId(1L);
    }

    @Test
    void markAttendance_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Teacher teacher = Teacher.builder().id(1L).fullName("Teacher A").build();
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            when(academicYearRepository.findActiveBySchool(school)).thenReturn(ay);

            Student student = Student.builder().id(10L).fullName("Student A").build();
            when(studentRepository.findById(10L)).thenReturn(Optional.of(student));

            when(studentAttendanceRepository.findByStudentIdAndDate(10L, LocalDate.now()))
                    .thenReturn(Optional.empty());
            when(studentAttendanceRepository.save(any())).thenReturn(null);

            var entry = new MarkAttendanceRequest.StudentAttendanceEntry(10L, AttendanceStatus.PRESENT);
            var request = new MarkAttendanceRequest(1L, "A", LocalDate.now(), List.of(entry));

            attendanceService.markAttendance(request, 1L);

            verify(studentAttendanceRepository).save(any());
        }
    }

    @Test
    void markAttendance_futureDate_throwsBadRequest() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Teacher teacher = Teacher.builder().id(1L).fullName("Teacher A").build();
            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            when(academicYearRepository.findActiveBySchool(school)).thenReturn(ay);

            LocalDate futureDate = LocalDate.now().plusDays(5);
            var entry = new MarkAttendanceRequest.StudentAttendanceEntry(10L, AttendanceStatus.PRESENT);
            var request = new MarkAttendanceRequest(1L, "A", futureDate, List.of(entry));

            assertThrows(BadRequestException.class, () -> attendanceService.markAttendance(request, 1L));
        }
    }

    @Test
    void getStudentAttendanceSummary_calculatesCorrectly() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Student student = Student.builder().id(1L).fullName("Student A").build();
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            YearMonth month = YearMonth.of(2024, 10);
            LocalDate startDate = month.atDay(1);
            LocalDate endDate = month.atEndOfMonth();

            when(studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
                    eq(1L), eq(startDate), eq(endDate), eq(AttendanceStatus.PRESENT))).thenReturn(18L);
            when(studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
                    eq(1L), eq(startDate), eq(endDate), eq(AttendanceStatus.ABSENT))).thenReturn(1L);
            when(studentAttendanceRepository.countByStudentIdAndDateRangeAndStatus(
                    eq(1L), eq(startDate), eq(endDate), eq(AttendanceStatus.LEAVE))).thenReturn(1L);

            AttendanceSummary summary = attendanceService.getStudentAttendanceSummary(1L, month);

            assertEquals(20, summary.totalDays());
            assertEquals(18, summary.presentDays());
            assertEquals(1, summary.absentDays());
            assertEquals(1, summary.leaveDays());
            assertEquals(new BigDecimal("90.00"), summary.attendancePercentage());
        }
    }

    @Test
    void markTeacherSelfAttendance_success() {
        Teacher teacher = Teacher.builder().id(1L).fullName("Teacher A").build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        assertDoesNotThrow(() -> attendanceService.markTeacherSelfAttendance(1L));
    }

    @Test
    void markTeacherSelfAttendance_notFound_throws() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> attendanceService.markTeacherSelfAttendance(99L));
    }
}
