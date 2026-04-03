package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateFeeStructureRequest;
import com.portal.studymate.accounts.dtos.responses.FeeStructureResponse;
import com.portal.studymate.accounts.dtos.responses.StudentFeeResponse;
import com.portal.studymate.accounts.service.FeeStructureService;
import com.portal.studymate.accounts.service.PaymentService;
import com.portal.studymate.accounts.service.ReportsService;
import com.portal.studymate.accounts.service.StudentFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsControllerTest {

    @Mock private FeeStructureService feeStructureService;
    @Mock private StudentFeeService studentFeeService;
    @Mock private PaymentService paymentService;
    @Mock private ReportsService reportsService;
    @Mock private Authentication authentication;

    @InjectMocks private AccountsController controller;

    @Test
    void createFeeStructure_returnsOk() {
        FeeStructureResponse response = mock(FeeStructureResponse.class);
        when(feeStructureService.createFeeStructure(any())).thenReturn(response);
        when(authentication.getName()).thenReturn("admin");

        ResponseEntity<FeeStructureResponse> result = controller.createFeeStructure(
            mock(CreateFeeStructureRequest.class), authentication);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void getStudentFees_returnsOk() {
        Page<StudentFeeResponse> page = new PageImpl<>(List.of());
        when(studentFeeService.getStudentFees(eq(1L), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<StudentFeeResponse>> result = controller.getStudentFees(1L, Pageable.unpaged());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void deleteFeeStructure_returnsNoContent() {
        doNothing().when(feeStructureService).deleteFeeStructure(1L);
        ResponseEntity<Void> result = controller.deleteFeeStructure(1L);
        assertEquals(204, result.getStatusCode().value());
    }
}
