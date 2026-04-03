package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateFeeDiscountRequest;
import com.portal.studymate.accounts.dtos.responses.FeeDiscountResponse;
import com.portal.studymate.accounts.service.FeeDiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts/fee-discounts")
@RequiredArgsConstructor
@Tag(name = "Fee Discounts", description = "Manage student fee discounts and concessions")
public class FeeDiscountController {

    private final FeeDiscountService feeDiscountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create fee discount for student")
    public ResponseEntity<FeeDiscountResponse> create(@Valid @RequestBody CreateFeeDiscountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feeDiscountService.createDiscount(request));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACCOUNTANT')")
    @Operation(summary = "Get student discounts")
    public ResponseEntity<List<FeeDiscountResponse>> getStudentDiscounts(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeDiscountService.getStudentDiscounts(studentId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate discount")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feeDiscountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
}
