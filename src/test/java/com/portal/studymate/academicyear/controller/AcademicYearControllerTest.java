package com.portal.studymate.academicyear.controller;

import com.portal.studymate.academicyear.dto.AcademicYearResponse;
import com.portal.studymate.academicyear.dto.CreateAcademicYearRequest;
import com.portal.studymate.academicyear.service.AcademicYearService;
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
class AcademicYearControllerTest {

    @Mock private AcademicYearService service;
    @InjectMocks private AcademicYearController controller;

    @Test
    void create_returnsOk() {
        AcademicYearResponse response = AcademicYearResponse.builder().id(1L).year("2024-2025").status("ACTIVE").build();
        when(service.createAcademicYear(any(CreateAcademicYearRequest.class))).thenReturn(response);

        ResponseEntity<AcademicYearResponse> result = controller.create(new CreateAcademicYearRequest());
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
    }

    @Test
    void list_returnsOk() {
        when(service.getAllAcademicYears()).thenReturn(List.of(AcademicYearResponse.builder().id(1L).year("2024-2025").status("ACTIVE").build()));

        ResponseEntity<List<AcademicYearResponse>> result = controller.list();
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void activateAcademicYear_returnsOk() {
        AcademicYearResponse response = AcademicYearResponse.builder().id(1L).year("2024-2025").status("ACTIVE").build();
        when(service.activateAcademicYear(1L)).thenReturn(response);

        ResponseEntity<AcademicYearResponse> result = controller.activateAcademicYear(1L);
        assertEquals(200, result.getStatusCode().value());
    }
}
