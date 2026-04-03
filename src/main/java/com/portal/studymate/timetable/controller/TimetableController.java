package com.portal.studymate.timetable.controller;

import com.portal.studymate.timetable.dto.*;
import com.portal.studymate.timetable.service.TimetableService;
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
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Tag(name = "Timetable Management", description = "APIs for managing school timetable")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping("/periods")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create period definition")
    public ResponseEntity<PeriodDefinitionResponse> createPeriod(
            @Valid @RequestBody CreatePeriodDefinitionRequest request) {
        log.info("POST /api/timetable/periods - Creating period");
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createPeriod(request));
    }

    @GetMapping("/periods")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all period definitions")
    public ResponseEntity<List<PeriodDefinitionResponse>> getPeriods() {
        log.info("GET /api/timetable/periods - Getting periods");
        return ResponseEntity.ok(timetableService.getPeriods());
    }

    @DeleteMapping("/periods/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete period definition")
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        log.info("DELETE /api/timetable/periods/{} - Deleting period", id);
        timetableService.deletePeriod(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entries")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create timetable entry")
    public ResponseEntity<TimetableEntryResponse> createEntry(
            @Valid @RequestBody CreateTimetableEntryRequest request) {
        log.info("POST /api/timetable/entries - Creating entry");
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createEntry(request));
    }

    @GetMapping("/class/{classId}/section/{section}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Get class timetable")
    public ResponseEntity<List<TimetableEntryResponse>> getClassTimetable(
            @PathVariable Long classId, @PathVariable String section) {
        log.info("GET /api/timetable/class/{}/section/{} - Getting class timetable", classId, section);
        return ResponseEntity.ok(timetableService.getClassTimetable(classId, section));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get teacher timetable")
    public ResponseEntity<List<TimetableEntryResponse>> getTeacherTimetable(@PathVariable Long teacherId) {
        log.info("GET /api/timetable/teacher/{} - Getting teacher timetable", teacherId);
        return ResponseEntity.ok(timetableService.getTeacherTimetable(teacherId));
    }

    @DeleteMapping("/entries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete timetable entry")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        log.info("DELETE /api/timetable/entries/{} - Deleting entry", id);
        timetableService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
