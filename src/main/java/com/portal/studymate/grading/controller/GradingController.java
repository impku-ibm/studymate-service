package com.portal.studymate.grading.controller;

import com.portal.studymate.grading.dto.CreateGradingScaleRequest;
import com.portal.studymate.grading.dto.GradingScaleResponse;
import com.portal.studymate.grading.service.GradingService;
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
@RequestMapping("/api/grading-scales")
@RequiredArgsConstructor
@Tag(name = "Grading System", description = "Configurable grading scales per school")
public class GradingController {

    private final GradingService gradingService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create grading scale")
    public ResponseEntity<GradingScaleResponse> create(
            @Valid @RequestBody CreateGradingScaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradingService.createGradingScale(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all grading scales")
    public ResponseEntity<List<GradingScaleResponse>> getAll() {
        return ResponseEntity.ok(gradingService.getGradingScales());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete grading scale")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradingService.deleteGradingScale(id);
        return ResponseEntity.noContent().build();
    }
}
