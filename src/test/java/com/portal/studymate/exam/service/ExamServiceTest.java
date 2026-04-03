package com.portal.studymate.exam.service;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.BadRequestException;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.exam.dto.*;
import com.portal.studymate.exam.enums.ExamStatus;
import com.portal.studymate.exam.enums.ExamType;
import com.portal.studymate.exam.model.Exam;
import com.portal.studymate.exam.model.ExamSchedule;
import com.portal.studymate.exam.repository.ExamMarksRepository;
import com.portal.studymate.exam.repository.ExamRepository;
import com.portal.studymate.exam.repository.ExamScheduleRepository;
import com.portal.studymate.exam.repository.StudentResultRepository;
import com.portal.studymate.grading.service.GradingService;
import com.portal.studymate.school.model.School;
import com.portal.studymate.student.model.Student;
import com.portal.studymate.student.repository.StudentRepository;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock private ExamRepository examRepository;
    @Mock private ExamScheduleRepository examScheduleRepository;
    @Mock private ExamMarksRepository examMarksRepository;
    @Mock private StudentResultRepository studentResultRepository;
    @Mock private AcademicYearRepository academicYearRepository;
    @Mock private SchoolClassRepository schoolClassRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private ApplicationEventPublisher applicationEventPublisher;
    @Mock private GradingService gradingService;

    private ExamService examService;
    private School school;

    @BeforeEach
    void setUp() {
        examService = new ExamService(examRepository, examScheduleRepository, examMarksRepository,
                studentResultRepository, academicYearRepository, schoolClassRepository,
                subjectRepository, studentRepository, teacherRepository,
                applicationEventPublisher, gradingService);
        school = new School();
        school.setId(1L);
    }

    @Test
    void createExam_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            when(academicYearRepository.findById(1L)).thenReturn(Optional.of(ay));
            when(examRepository.existsBySchoolIdAndAcademicYearIdAndExamType(
                    eq(1L), eq(1L), eq(ExamType.MID_TERM))).thenReturn(false);
            when(examRepository.save(any(Exam.class))).thenAnswer(inv -> {
                Exam e = inv.getArgument(0);
                e.setId(1L);
                return e;
            });

            var request = new CreateExamRequest(1L, ExamType.MID_TERM, "Mid Term 2024",
                    LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 15));

            ExamResponse result = examService.createExam(request);

            assertNotNull(result);
            assertEquals("Mid Term 2024", result.name());
            assertEquals(ExamType.MID_TERM, result.examType());
            verify(examRepository).save(any(Exam.class));
        }
    }

    @Test
    void createExam_duplicateType_throwsConflict() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            when(academicYearRepository.findById(1L)).thenReturn(Optional.of(ay));
            when(examRepository.existsBySchoolIdAndAcademicYearIdAndExamType(
                    eq(1L), eq(1L), eq(ExamType.MID_TERM))).thenReturn(true);

            var request = new CreateExamRequest(1L, ExamType.MID_TERM, "Mid Term 2024",
                    LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 15));

            assertThrows(ConflictException.class, () -> examService.createExam(request));
        }
    }

    @Test
    void createExam_endBeforeStart_throwsBadRequest() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            when(academicYearRepository.findById(1L)).thenReturn(Optional.of(ay));
            when(examRepository.existsBySchoolIdAndAcademicYearIdAndExamType(
                    eq(1L), eq(1L), eq(ExamType.MID_TERM))).thenReturn(false);

            var request = new CreateExamRequest(1L, ExamType.MID_TERM, "Mid Term 2024",
                    LocalDate.of(2024, 10, 15), LocalDate.of(2024, 10, 1));

            assertThrows(BadRequestException.class, () -> examService.createExam(request));
        }
    }

    @Test
    void enterMarks_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            ExamSchedule schedule = new ExamSchedule();
            schedule.setId(1L);
            schedule.setMaxMarks(100);
            when(examScheduleRepository.findByIdAndSchoolId(1L, 1L)).thenReturn(Optional.of(schedule));

            Student student = Student.builder().id(1L).fullName("Test Student").build();
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            Teacher teacher = new Teacher();
            teacher.setId(10L);
            when(teacherRepository.findById(10L)).thenReturn(Optional.of(teacher));

            when(examMarksRepository.findByExamScheduleIdAndStudentId(1L, 1L)).thenReturn(Optional.empty());
            when(examMarksRepository.save(any())).thenReturn(null);

            var request = new EnterMarksRequest(1L, 1L, 85, false, null);
            examService.enterMarks(request, 10L);

            verify(examMarksRepository).save(any());
        }
    }

    @Test
    void enterMarks_exceedsMax_throwsBadRequest() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            ExamSchedule schedule = new ExamSchedule();
            schedule.setId(1L);
            schedule.setMaxMarks(100);
            when(examScheduleRepository.findByIdAndSchoolId(1L, 1L)).thenReturn(Optional.of(schedule));

            Student student = Student.builder().id(1L).fullName("Test Student").build();
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

            Teacher teacher = new Teacher();
            teacher.setId(10L);
            when(teacherRepository.findById(10L)).thenReturn(Optional.of(teacher));

            var request = new EnterMarksRequest(1L, 1L, 150, false, null);

            assertThrows(BadRequestException.class, () -> examService.enterMarks(request, 10L));
        }
    }

    @Test
    void getAllExams_returnsSchoolExams() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            AcademicYear ay = AcademicYear.builder().id(1L).year("2024-25").build();
            Exam exam1 = new Exam(school, ay, ExamType.MID_TERM, "Mid Term",
                    LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 15));
            exam1.setId(1L);
            Exam exam2 = new Exam(school, ay, ExamType.FINAL, "Final",
                    LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 15));
            exam2.setId(2L);

            when(examRepository.findBySchoolId(1L)).thenReturn(List.of(exam1, exam2));

            List<ExamResponse> result = examService.getAllExams();

            assertEquals(2, result.size());
            assertEquals("Mid Term", result.get(0).name());
            assertEquals("Final", result.get(1).name());
        }
    }

    @Test
    void getSchedulesForExam_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Exam exam = new Exam();
            exam.setId(1L);
            when(examRepository.findByIdAndSchoolId(1L, 1L)).thenReturn(Optional.of(exam));
            when(examScheduleRepository.findByExamId(1L)).thenReturn(List.of());

            List<ExamScheduleResponse> result = examService.getSchedulesForExam(1L);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(examScheduleRepository).findByExamId(1L);
        }
    }

    @Test
    void addGraceMarks_publishedExam_throwsBadRequest() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);

            Exam exam = new Exam();
            exam.setId(1L);
            exam.setStatus(ExamStatus.PUBLISHED);
            when(examRepository.findByIdAndSchoolId(1L, 1L)).thenReturn(Optional.of(exam));

            var request = new GraceMarkRequest(List.of(
                    new GraceMarkRequest.GraceMarkEntry(1L, 1L, 5)));

            assertThrows(BadRequestException.class, () -> examService.addGraceMarks(1L, request));
        }
    }
}
