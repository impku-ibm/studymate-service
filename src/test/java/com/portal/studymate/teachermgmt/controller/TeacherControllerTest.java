package com.portal.studymate.teachermgmt.controller;

import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {
    @Mock private TeacherService teacherService;
    @InjectMocks private TeacherController controller;

    @Test
    void createTeacher_returnsResponse() {
        var response = TeacherResponse.builder().id(1L).fullName("Teacher A").email("a@test.com").active(true).build();
        when(teacherService.createTeacher(any())).thenReturn(response);
        var result = controller.createTeacher(new CreateTeacherRequest());
        assertEquals("Teacher A", result.getFullName());
    }

    @Test
    void getAllTeachers_returnsList() {
        when(teacherService.getAllTeachers()).thenReturn(List.of(
            TeacherResponse.builder().id(1L).fullName("A").build()));
        assertEquals(1, controller.getAllTeachers().size());
    }
}
