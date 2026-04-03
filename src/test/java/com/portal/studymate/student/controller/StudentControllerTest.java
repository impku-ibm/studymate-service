package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.*;
import com.portal.studymate.student.model.StudentStatus;
import com.portal.studymate.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @Mock private StudentService studentService;
    @InjectMocks private StudentController controller;

    @Test
    void create_returnsStudent() {
        var response = StudentResponse.builder().id(1L).fullName("Test").admissionNumber("ADM001").status(StudentStatus.ACTIVE).build();
        when(studentService.createStudent(any())).thenReturn(response);
        var result = controller.create(new CreateStudentRequest());
        assertEquals("Test", result.getFullName());
    }

    @Test
    void list_returnsAll() {
        when(studentService.getAllStudents()).thenReturn(List.of(
            StudentResponse.builder().id(1L).fullName("A").build(),
            StudentResponse.builder().id(2L).fullName("B").build()));
        var result = controller.list();
        assertEquals(2, result.size());
    }
}
