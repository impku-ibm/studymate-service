package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.GenerateTCRequest;
import com.portal.studymate.student.dto.TransferCertificateResponse;
import com.portal.studymate.student.service.TransferCertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Transfer Certificate", description = "Generate transfer certificates")
public class TransferCertificateController {

    private final TransferCertificateService tcService;

    @PostMapping("/{studentId}/transfer-certificate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate transfer certificate for student")
    public ResponseEntity<TransferCertificateResponse> generateTC(
            @PathVariable Long studentId,
            @Valid @RequestBody GenerateTCRequest request) {
        log.info("POST /students/{}/transfer-certificate - Generating TC", studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tcService.generateTC(studentId, request));
    }

    @GetMapping("/{studentId}/transfer-certificate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get transfer certificate for student")
    public ResponseEntity<TransferCertificateResponse> getTC(@PathVariable Long studentId) {
        log.info("GET /students/{}/transfer-certificate - Getting TC", studentId);
        return ResponseEntity.ok(tcService.getTC(studentId));
    }
}
