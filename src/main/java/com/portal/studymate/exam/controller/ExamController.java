package com.portal.studymate.exam.controller;

import com.portal.studymate.exam.dto.*;
import com.portal.studymate.exam.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exam Management", description = "APIs for managing exams and marks")
public class ExamController {
    
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new exam")
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody CreateExamRequest request) {
        var response = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Schedule exam for class and subject")
    public ResponseEntity<ExamResponse> scheduleExam(@Valid @RequestBody CreateExamScheduleRequest request) {
        var response = examService.scheduleExam(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/marks")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Enter or update student marks")
    public ResponseEntity<Void> enterMarks(@Valid @RequestBody EnterMarksRequest request,
                                          Authentication authentication) {
        // Extract teacher ID from authentication context
        Long teacherId = extractTeacherIdFromAuth(authentication);
        examService.enterMarks(request, teacherId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{examId}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Publish exam results")
    public ResponseEntity<Void> publishResults(@PathVariable Long examId) {
        examService.publishResults(examId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get all exams")
    public ResponseEntity<List<ExamResponse>> getAllExams() {
        var exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{examId}/schedules")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get exam schedules")
    public ResponseEntity<List<ExamScheduleResponse>> getSchedules(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getSchedulesForExam(examId));
    }

    @GetMapping("/{examId}/results")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Get exam results")
    public ResponseEntity<List<StudentResultResponse>> getResults(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getClassResults(examId));
    }

    @PostMapping("/{examId}/grace-marks")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add grace marks before publishing")
    public ResponseEntity<Void> addGraceMarks(@PathVariable Long examId,
                                               @Valid @RequestBody GraceMarkRequest request) {
        examService.addGraceMarks(examId, request);
        return ResponseEntity.ok().build();
    }

    private Long extractTeacherIdFromAuth(Authentication authentication) {
        // Implementation depends on your authentication setup
        // This is a placeholder - implement based on your JWT structure
        return 1L; // Replace with actual teacher ID extraction
    }
}