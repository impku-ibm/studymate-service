package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateFeePlanRequest;
import com.portal.studymate.accounts.dtos.responses.FeePlanResponse;
import com.portal.studymate.accounts.service.FeePlanService;
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
class FeePlanControllerTest {

    @Mock private FeePlanService feePlanService;
    @InjectMocks private FeePlanController controller;

    @Test
    void create_returnsCreated() {
        FeePlanResponse response = mock(FeePlanResponse.class);
        when(feePlanService.createFeePlan(any(CreateFeePlanRequest.class))).thenReturn(response);

        ResponseEntity<FeePlanResponse> result = controller.create(new CreateFeePlanRequest("Test Plan", "Description", List.of()));
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void getAll_returnsOk() {
        when(feePlanService.getFeePlans()).thenReturn(List.of());

        ResponseEntity<List<FeePlanResponse>> result = controller.getAll();
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(feePlanService).deleteFeePlan(1L);
        ResponseEntity<Void> result = controller.delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }
}
