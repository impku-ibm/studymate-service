package com.portal.studymate.teacherassignment.service;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.ClassSectionTemplate;
import com.portal.studymate.classmanagement.repository.ClassSectionTemplateRepository;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;
import com.portal.studymate.teacherassignment.model.TeacherAssignment;
import com.portal.studymate.teacherassignment.repository.TeacherAssignmentRepository;
import com.portal.studymate.teacherassignment.service.impl.TeacherAssignmentServiceImpl;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherAssignmentServiceTest {

    @Mock private TeacherAssignmentRepository assignmentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private ClassSectionTemplateRepository sectionRepository;
    @Mock private AcademicYearRepository academicYearRepository;

    @InjectMocks private TeacherAssignmentServiceImpl assignmentService;

    private School school;
    private AcademicYear activeYear;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").build();
        activeYear = AcademicYear.builder().id(1L).year("2024-2025").build();
    }

    @Test
    void assignTeacher_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.of(activeYear));

            Teacher teacher = Teacher.builder().id(1L).fullName("Teacher 1").build();
            Subject subject = Subject.builder().id(1L).name("Math").code("MATH").build();
            ClassSectionTemplate section = ClassSectionTemplate.builder().id(1L).name("A").build();

            when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));
            when(assignmentRepository.findByAcademicYearAndSectionIdAndSubjectId(activeYear, 1L, 1L))
                .thenReturn(Optional.empty());
            when(assignmentRepository.save(any(TeacherAssignment.class))).thenAnswer(inv -> {
                TeacherAssignment a = inv.getArgument(0);
                a.setId(1L);
                return a;
            });

            CreateTeacherAssignmentRequest request = new CreateTeacherAssignmentRequest();
            request.setTeacherId(1L);
            request.setSubjectId(1L);
            request.setSectionId(1L);

            TeacherAssignmentResponse result = assignmentService.assignTeacher(request);
            assertNotNull(result);
            verify(assignmentRepository).save(any(TeacherAssignment.class));
        }
    }

    @Test
    void assignTeacher_noActiveYear_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.empty());

            CreateTeacherAssignmentRequest request = new CreateTeacherAssignmentRequest();
            assertThrows(ResourceNotFoundException.class, () -> assignmentService.assignTeacher(request));
        }
    }

    @Test
    void getAssignmentsForSection_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.of(activeYear));

            Teacher teacher = Teacher.builder().id(1L).fullName("Teacher 1").build();
            Subject subject = Subject.builder().id(1L).name("Math").code("MATH").build();
            ClassSectionTemplate section = ClassSectionTemplate.builder().id(1L).name("A").build();

            TeacherAssignment assignment = TeacherAssignment.builder()
                .id(1L).teacher(teacher).subject(subject).section(section).active(true).build();
            when(assignmentRepository.findByAcademicYearAndSectionId(activeYear, 1L))
                .thenReturn(List.of(assignment));

            List<TeacherAssignmentResponse> result = assignmentService.getAssignmentsForSection(1L);
            assertEquals(1, result.size());
        }
    }
}
