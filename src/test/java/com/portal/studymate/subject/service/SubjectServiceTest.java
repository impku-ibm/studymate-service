package com.portal.studymate.subject.service;

import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;
import com.portal.studymate.subject.model.Subject;
import com.portal.studymate.subject.repository.SubjectRepository;
import com.portal.studymate.subject.service.impl.SubjectServiceImpl;
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
class SubjectServiceTest {

    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private SubjectServiceImpl subjectService;

    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").build();
    }

    @Test
    void createSubject_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(subjectRepository.findBySchoolAndCode(school, "MATH")).thenReturn(Optional.empty());
            when(subjectRepository.save(any(Subject.class))).thenAnswer(inv -> {
                Subject s = inv.getArgument(0);
                s.setId(1L);
                return s;
            });

            CreateSubjectRequest request = new CreateSubjectRequest();
            request.setName("Mathematics");
            request.setCode("MATH");

            SubjectResponse result = subjectService.createSubject(request);
            assertNotNull(result);
            verify(subjectRepository).save(any(Subject.class));
        }
    }

    @Test
    void createSubject_duplicate_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(subjectRepository.findBySchoolAndCode(school, "MATH"))
                .thenReturn(Optional.of(Subject.builder().id(1L).build()));

            CreateSubjectRequest request = new CreateSubjectRequest();
            request.setName("Mathematics");
            request.setCode("MATH");

            assertThrows(ConflictException.class, () -> subjectService.createSubject(request));
        }
    }

    @Test
    void getAllSubjects_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            Subject s = Subject.builder().id(1L).name("Math").code("MATH").active(true).build();
            when(subjectRepository.findBySchool(school)).thenReturn(List.of(s));

            List<SubjectResponse> result = subjectService.getAllSubjects();
            assertEquals(1, result.size());
        }
    }

    @Test
    void updateSubject_notFound_throws() {
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateSubjectRequest request = new UpdateSubjectRequest();
        assertThrows(ResourceNotFoundException.class, () -> subjectService.updateSubject(99L, request));
    }
}
