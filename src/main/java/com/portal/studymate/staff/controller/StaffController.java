package com.portal.studymate.staff.controller;

import com.portal.studymate.staff.dto.CreateStaffRequest;
import com.portal.studymate.staff.dto.StaffResponse;
import com.portal.studymate.staff.service.StaffService;
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
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@Tag(name = "Staff Management", description = "APIs for managing non-teaching staff")
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create staff member")
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody CreateStaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.createStaff(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all staff")
    public ResponseEntity<List<StaffResponse>> getAll() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update staff member")
    public ResponseEntity<StaffResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody CreateStaffRequest request) {
        log.info("PUT /api/staff/{} - Updating staff", id);
        return ResponseEntity.ok(staffService.updateStaff(id, request));
    }

    @PostMapping("/attendance/self")
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Staff marks own attendance")
    public ResponseEntity<Void> markSelfAttendance() {
        // TODO: extract staffId from JWT — using placeholder for now
        staffService.markSelfAttendance(1L);
        return ResponseEntity.ok().build();
    }
}
