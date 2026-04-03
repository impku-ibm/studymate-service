package com.portal.studymate.promotion.controller;

import com.portal.studymate.promotion.dto.BulkPromotionRequest;
import com.portal.studymate.promotion.dto.PromotionResultResponse;
import com.portal.studymate.promotion.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {
    @Mock private PromotionService promotionService;
    @InjectMocks private PromotionController controller;

    @Test
    void bulkPromote_returnsOk() {
        var response = new PromotionResultResponse(5, 0, List.of());
        when(promotionService.bulkPromote(any())).thenReturn(response);
        var result = controller.bulkPromote(new BulkPromotionRequest(1L, "A", 2L, "A", 1L, List.of(1L)));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(5, result.getBody().promotedCount());
    }
}
