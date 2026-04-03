package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateTransportEstimationRequest;
import com.portal.studymate.accounts.dtos.responses.TransportFeeEstimationResponse;
import com.portal.studymate.accounts.service.TransportFeeEstimationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transport-fee-estimation")
@Tag(name = "Transport Fee Estimation", description = "APIs for managing transport fee estimations")
public class TransportFeeController {
    
    private final TransportFeeEstimationService transportFeeEstimationService;

    public TransportFeeController(TransportFeeEstimationService transportFeeEstimationService) {
        this.transportFeeEstimationService = transportFeeEstimationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create transport fee estimation")
    public ResponseEntity<TransportFeeEstimationResponse> createEstimation(
            @Valid @RequestBody CreateTransportEstimationRequest request) {
        var response = transportFeeEstimationService.createEstimation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get all transport fee estimations")
    public ResponseEntity<List<TransportFeeEstimationResponse>> getAllEstimations() {
        var estimations = transportFeeEstimationService.getAllEstimations();
        return ResponseEntity.ok(estimations);
    }

    @GetMapping("/distance-slab/{distanceSlab}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get transport fee estimation by distance slab")
    public ResponseEntity<TransportFeeEstimationResponse> getEstimationByDistanceSlab(
            @PathVariable String distanceSlab) {
        var estimation = transportFeeEstimationService.getEstimationByDistanceSlab(distanceSlab);
        return ResponseEntity.ok(estimation);
    }
}