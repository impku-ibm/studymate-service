package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateFeeDiscountRequest;
import com.portal.studymate.accounts.dtos.responses.FeeDiscountResponse;
import com.portal.studymate.accounts.service.FeeDiscountService;
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
class FeeDiscountControllerTest {

    @Mock private FeeDiscountService feeDiscountService;
    @InjectMocks private FeeDiscountController controller;

    @Test
    void create_returnsCreated() {
        FeeDiscountResponse response = mock(FeeDiscountResponse.class);
        when(feeDiscountService.createDiscount(any(CreateFeeDiscountRequest.class))).thenReturn(response);

        ResponseEntity<FeeDiscountResponse> result = controller.create(new CreateFeeDiscountRequest(1L, 1L, null, null, null, "test"));
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    void getStudentDiscounts_returnsOk() {
        when(feeDiscountService.getStudentDiscounts(1L)).thenReturn(List.of());

        ResponseEntity<List<FeeDiscountResponse>> result = controller.getStudentDiscounts(1L);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(feeDiscountService).deleteDiscount(1L);
        ResponseEntity<Void> result = controller.delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }
}
