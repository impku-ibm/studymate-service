package com.portal.studymate.classsubject.service;

import com.portal.studymate.academicyear.model.AcademicYear;
import com.portal.studymate.academicyear.model.AcademicYearStatus;
import com.portal.studymate.academicyear.repository.AcademicYearRepository;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;
import com.portal.studymate.classsubject.model.ClassSubject;
import com.portal.studymate.classsubject.repository.ClassSubjectRepository;
import com.portal.studymate.classsubject.service.impl.ClassSubjectServiceImpl;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
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
class ClassSubjectServiceTest {

    @Mock private ClassSubjectRepository classSubjectRepository;
    @Mock private SchoolClassRepository classRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private AcademicYearRepository academicYearRepository;

    @InjectMocks private ClassSubjectServiceImpl classSubjectService;

    private School school;
    private AcademicYear activeYear;
    private SchoolClass schoolClass;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").build();
        activeYear = AcademicYear.builder().id(1L).year("2024-2025").build();
        schoolClass = SchoolClass.builder().id(1L).name("Class 10").build();
    }

    @Test
    void assignSubjects_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.of(activeYear));
            when(classRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

            Subject subject = Subject.builder().id(1L).name("Math").code("MATH").build();
            when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
            when(classSubjectRepository.existsByAcademicYearAndSchoolClassAndSubject(activeYear, schoolClass, subject))
                .thenReturn(false);
            when(classSubjectRepository.save(any(ClassSubject.class))).thenAnswer(inv -> {
                ClassSubject cs = inv.getArgument(0);
                cs.setId(1L);
                return cs;
            });

            AssignSubjectsRequest request = new AssignSubjectsRequest();
            request.setClassId(1L);
            request.setSubjectIds(List.of(1L));

            List<ClassSubjectResponse> result = classSubjectService.assignSubjects(request);
            assertEquals(1, result.size());
        }
    }

    @Test
    void assignSubjects_noActiveYear_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.empty());

            AssignSubjectsRequest request = new AssignSubjectsRequest();
            request.setClassId(1L);
            request.setSubjectIds(List.of(1L));

            assertThrows(ResourceNotFoundException.class, () -> classSubjectService.assignSubjects(request));
        }
    }

    @Test
    void getSubjectsForClass_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(academicYearRepository.findBySchoolAndStatus(school, AcademicYearStatus.ACTIVE))
                .thenReturn(Optional.of(activeYear));
            when(classRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

            Subject subject = Subject.builder().id(1L).name("Math").code("MATH").build();
            ClassSubject cs = ClassSubject.builder().id(1L).subject(subject).build();
            when(classSubjectRepository.findByAcademicYearAndSchoolClass(activeYear, schoolClass))
                .thenReturn(List.of(cs));

            List<ClassSubjectResponse> result = classSubjectService.getSubjectsForClass(1L);
            assertEquals(1, result.size());
        }
    }
}
