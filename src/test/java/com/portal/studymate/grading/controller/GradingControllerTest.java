package com.portal.studymate.grading.controller;

import com.portal.studymate.grading.dto.CreateGradingScaleRequest;
import com.portal.studymate.grading.dto.GradingScaleResponse;
import com.portal.studymate.grading.service.GradingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradingControllerTest {

    @Mock private GradingService gradingService;
    @InjectMocks private GradingController controller;

    @Test
    void create_returnsCreated() {
        GradingScaleResponse response = mock(GradingScaleResponse.class);
        when(gradingService.createGradingScale(any(CreateGradingScaleRequest.class))).thenReturn(response);

        ResponseEntity<GradingScaleResponse> result = controller.create(new CreateGradingScaleRequest("Default Scale", true, List.of()));
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void getAll_returnsOk() {
        when(gradingService.getGradingScales()).thenReturn(List.of());

        ResponseEntity<List<GradingScaleResponse>> result = controller.getAll();
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(gradingService).deleteGradingScale(1L);
        ResponseEntity<Void> result = controller.delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }
}
