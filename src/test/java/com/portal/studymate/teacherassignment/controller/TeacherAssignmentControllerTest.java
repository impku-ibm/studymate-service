package com.portal.studymate.teacherassignment.controller;

import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;
import com.portal.studymate.teacherassignment.service.TeacherAssignmentService;
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
class TeacherAssignmentControllerTest {
    @Mock private TeacherAssignmentService service;
    @InjectMocks private TeacherAssignmentController controller;

    @Test
    void assignTeacher_returnsResponse() {
        var response = TeacherAssignmentResponse.builder().id(1L).build();
        when(service.assignTeacher(any())).thenReturn(response);
        var result = controller.assignTeacher(new CreateTeacherAssignmentRequest());
        assertNotNull(result);
    }

    @Test
    void getAssignmentsForSection_returnsList() {
        when(service.getAssignmentsForSection(1L)).thenReturn(List.of());
        assertEquals(0, controller.getAssignmentsForSection(1L).size());
    }
}
