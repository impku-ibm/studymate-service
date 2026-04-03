package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.GenerateTCRequest;
import com.portal.studymate.student.dto.TransferCertificateResponse;
import com.portal.studymate.student.service.TransferCertificateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferCertificateControllerTest {
    @Mock private TransferCertificateService tcService;
    @InjectMocks private TransferCertificateController controller;

    @Test
    void generateTC_returnsCreated() {
        var response = TransferCertificateResponse.builder().id(1L).tcNumber("TC-001").studentName("Test").build();
        when(tcService.generateTC(eq(1L), any())).thenReturn(response);
        var result = controller.generateTC(1L, new GenerateTCRequest());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("TC-001", result.getBody().getTcNumber());
    }

    @Test
    void getTC_returnsOk() {
        var response = TransferCertificateResponse.builder().id(1L).tcNumber("TC-001").build();
        when(tcService.getTC(1L)).thenReturn(response);
        var result = controller.getTC(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
