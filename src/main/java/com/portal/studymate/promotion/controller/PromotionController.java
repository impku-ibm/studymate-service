package com.portal.studymate.promotion.controller;

import com.portal.studymate.promotion.dto.BulkPromotionRequest;
import com.portal.studymate.promotion.dto.PromotionResultResponse;
import com.portal.studymate.promotion.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@Tag(name = "Student Promotion", description = "Bulk student promotion APIs")
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk promote students to next class")
    public ResponseEntity<PromotionResultResponse> bulkPromote(
            @Valid @RequestBody BulkPromotionRequest request) {
        return ResponseEntity.ok(promotionService.bulkPromote(request));
    }
}
