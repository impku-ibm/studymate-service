package com.portal.studymate.classmanagement.service;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;
import com.portal.studymate.classmanagement.model.SchoolClass;
import com.portal.studymate.classmanagement.repository.SchoolClassRepository;
import com.portal.studymate.classmanagement.service.impl.ClassServiceImpl;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
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
class ClassServiceTest {

    @Mock private SchoolClassRepository classRepository;
    @InjectMocks private ClassServiceImpl classService;

    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").build();
    }

    @Test
    void createClass_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(classRepository.findBySchoolAndName(school, "Class 10")).thenReturn(Optional.empty());
            when(classRepository.save(any(SchoolClass.class))).thenAnswer(inv -> {
                SchoolClass sc = inv.getArgument(0);
                sc.setId(1L);
                return sc;
            });

            CreateClassRequest request = new CreateClassRequest();
            request.setName("Class 10");

            ClassResponse result = classService.createClass(request);
            assertNotNull(result);
            verify(classRepository).save(any(SchoolClass.class));
        }
    }

    @Test
    void createClass_duplicate_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(classRepository.findBySchoolAndName(school, "Class 10"))
                .thenReturn(Optional.of(SchoolClass.builder().id(1L).build()));

            CreateClassRequest request = new CreateClassRequest();
            request.setName("Class 10");

            assertThrows(ConflictException.class, () -> classService.createClass(request));
        }
    }

    @Test
    void getAllClasses_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            SchoolClass sc = SchoolClass.builder().id(1L).name("Class 10").active(true).build();
            when(classRepository.findBySchool(school)).thenReturn(List.of(sc));

            List<ClassResponse> result = classService.getAllClasses();
            assertEquals(1, result.size());
        }
    }

    @Test
    void updateClass_notFound_throws() {
        when(classRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateClassRequest request = new UpdateClassRequest();
        assertThrows(ResourceNotFoundException.class, () -> classService.updateClass(99L, request));
    }
}
