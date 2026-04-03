package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.EnrollStudentRequest;
import com.portal.studymate.student.dto.StudentEnrollmentResponse;
import com.portal.studymate.student.service.StudentEnrollmentService;
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
class StudentEnrollmentControllerTest {
    @Mock private StudentEnrollmentService service;
    @InjectMocks private StudentEnrollmentController controller;

    @Test
    void enroll_returnsResponse() {
        var response = StudentEnrollmentResponse.builder().studentName("Test").className("Class 10").build();
        when(service.enroll(any())).thenReturn(response);
        var result = controller.enroll(new EnrollStudentRequest());
        assertEquals("Test", result.getStudentName());
    }

    @Test
    void list_returnsList() {
        when(service.listActiveYear()).thenReturn(List.of());
        assertEquals(0, controller.list().size());
    }
}
