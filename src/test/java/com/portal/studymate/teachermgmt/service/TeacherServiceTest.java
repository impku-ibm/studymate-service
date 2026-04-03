package com.portal.studymate.teachermgmt.service;

import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ConflictException;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.school.model.School;
import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.dto.UpdateTeacherRequest;
import com.portal.studymate.teachermgmt.model.Teacher;
import com.portal.studymate.teachermgmt.repository.TeacherRepository;
import com.portal.studymate.teachermgmt.service.impl.TeacherServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock private TeacherRepository teacherRepository;
    @Mock private AuthService authServiceClient;
    @InjectMocks private TeacherServiceImpl teacherService;

    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder().id(1L).name("Test School").schoolCode("TST").build();
    }

    @Test
    void createTeacher_success() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(teacherRepository.findBySchoolAndEmail(school, "teacher@test.com")).thenReturn(Optional.empty());
            when(authServiceClient.createTeacherUser(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("user-id-1");
            when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> {
                Teacher t = inv.getArgument(0);
                t.setId(1L);
                return t;
            });

            CreateTeacherRequest request = new CreateTeacherRequest();
            request.setFullName("Test Teacher");
            request.setEmail("teacher@test.com");
            request.setMobileNumber("9876543210");

            TeacherResponse result = teacherService.createTeacher(request);
            assertNotNull(result);
            assertEquals("Test Teacher", result.getFullName());
        }
    }

    @Test
    void createTeacher_duplicate_throws() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            when(teacherRepository.findBySchoolAndEmail(school, "dup@test.com"))
                .thenReturn(Optional.of(Teacher.builder().id(1L).build()));

            CreateTeacherRequest request = new CreateTeacherRequest();
            request.setEmail("dup@test.com");

            assertThrows(ConflictException.class, () -> teacherService.createTeacher(request));
        }
    }

    @Test
    void getAllTeachers_returnsList() {
        try (MockedStatic<SchoolContext> ctx = mockStatic(SchoolContext.class)) {
            ctx.when(SchoolContext::getSchool).thenReturn(school);
            Teacher t = Teacher.builder().id(1L).fullName("Teacher 1").email("t@test.com")
                .phone("123").teacherCode("T-001").active(true).build();
            when(teacherRepository.findBySchool(school)).thenReturn(List.of(t));

            List<TeacherResponse> result = teacherService.getAllTeachers();
            assertEquals(1, result.size());
        }
    }

    @Test
    void updateTeacher_notFound_throws() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateTeacherRequest request = UpdateTeacherRequest.builder().fullName("Updated").build();
        assertThrows(ResourceNotFoundException.class, () -> teacherService.updateTeacher(99L, request));
    }
}
