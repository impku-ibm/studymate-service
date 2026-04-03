package com.portal.studymate.exam.controller;

import com.portal.studymate.exam.dto.CreateExamRequest;
import com.portal.studymate.exam.dto.ExamResponse;
import com.portal.studymate.exam.dto.EnterMarksRequest;
import com.portal.studymate.exam.service.ExamService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamControllerTest {

    @Mock private ExamService examService;
    @InjectMocks private ExamController controller;

    @Test
    void createExam_returnsCreated() {
        ExamResponse response = mock(ExamResponse.class);
        when(examService.createExam(any(CreateExamRequest.class))).thenReturn(response);

        ResponseEntity<ExamResponse> result = controller.createExam(new CreateExamRequest(1L, null, "Midterm", LocalDate.now(), LocalDate.now().plusDays(7)));
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void enterMarks_returnsOk() {
        doNothing().when(examService).enterMarks(any(EnterMarksRequest.class), any());
        Authentication auth = mock(Authentication.class);

        ResponseEntity<Void> result = controller.enterMarks(new EnterMarksRequest(1L, 1L, 85, false, "Good"), auth);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void getAllExams_returnsOk() {
        when(examService.getAllExams()).thenReturn(List.of());

        ResponseEntity<List<ExamResponse>> result = controller.getAllExams();
        assertEquals(200, result.getStatusCode().value());
    }
}
