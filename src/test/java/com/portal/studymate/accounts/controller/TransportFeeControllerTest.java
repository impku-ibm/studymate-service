package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.responses.TransportFeeEstimationResponse;
import com.portal.studymate.accounts.service.TransportFeeEstimationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportFeeControllerTest {
    @Mock private TransportFeeEstimationService service;
    @InjectMocks private TransportFeeController controller;

    @Test
    void getAllEstimations_returnsOk() {
        when(service.getAllEstimations()).thenReturn(List.of());
        var result = controller.getAllEstimations();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
