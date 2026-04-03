package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.ConfigureStudentFeesRequest;
import com.portal.studymate.accounts.dtos.responses.StudentFeeConfigurationResponse;
import com.portal.studymate.accounts.enums.FeeType;
import com.portal.studymate.accounts.service.StudentFeeConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing student fee configurations.
 * 
 * Handles:
 * - Configuring which fee types apply to each student
 * - Viewing student's active fee types
 * - Activating/deactivating fee types
 * 
 * Used by:
 * - Admission staff during student enrollment
 * - Accountants for fee management
 * - Parents/Students to view applicable fees
 */
@RestController
@RequestMapping("/api/v1/accounts/student-fee-config")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Fee Configuration", description = "Manage which fee types apply to each student")
public class StudentFeeConfigurationController {

   private final StudentFeeConfigurationService feeConfigService;

   /**
    * Configure fee types for a student.
    * 
    * Use cases:
    * 1. During admission - set initial fee types
    * 2. Mid-year - add/remove fee types (e.g., opt out of transport)
    * 
    * Example request:
    * POST /api/v1/accounts/student-fee-config/123
    * {
    *   "academicYearId": 1,
    *   "feeTypes": ["TUITION", "TRANSPORT", "EXAM"]
    * }
    */
   @PostMapping("/{studentId}")
   @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
   @Operation(summary = "Configure fee types for a student")
   public ResponseEntity<List<StudentFeeConfigurationResponse>> configureStudentFees(
         @PathVariable Long studentId,
         @Valid @RequestBody ConfigureStudentFeesRequest request,
         Authentication auth) {
      
      log.info("Configuring fees for student {} - User: {}, Fee Types: {}", 
         studentId, auth.getName(), request.feeTypes());
      
      List<StudentFeeConfigurationResponse> configs = feeConfigService
         .configureStudentFees(studentId, request);
      
      return ResponseEntity.ok(configs);
   }

   /**
    * Get student's fee configuration.
    * Shows which fee types are applicable (active and inactive).
    * 
    * Used in:
    * - Fee management UI
    * - Student/Parent portal to see applicable fees
    * - Accountant dashboard
    */
   @GetMapping("/{studentId}")
   @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT', 'TEACHER', 'PARENT', 'STUDENT')")
   @Operation(summary = "Get student's fee configuration")
   public ResponseEntity<List<StudentFeeConfigurationResponse>> getStudentFeeConfiguration(
         @PathVariable Long studentId,
         @RequestParam Long academicYearId) {
      
      List<StudentFeeConfigurationResponse> configs = feeConfigService
         .getStudentFeeConfigurations(studentId, academicYearId);
      
      return ResponseEntity.ok(configs);
   }

   /**
    * Deactivate a specific fee type for a student.
    * 
    * Use case: Student opts out of transport/hostel mid-year
    * 
    * Example: DELETE /api/v1/accounts/student-fee-config/123/fee-type/TRANSPORT?academicYearId=1
    */
   @DeleteMapping("/{studentId}/fee-type/{feeType}")
   @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
   @Operation(summary = "Deactivate a fee type for a student")
   public ResponseEntity<Void> deactivateFeeType(
         @PathVariable Long studentId,
         @PathVariable FeeType feeType,
         @RequestParam Long academicYearId,
         Authentication auth) {
      
      log.info("Deactivating fee type {} for student {} - User: {}", 
         feeType, studentId, auth.getName());
      
      feeConfigService.deactivateFeeType(studentId, academicYearId, feeType);
      
      return ResponseEntity.ok().build();
   }

   /**
    * Activate a previously deactivated fee type.
    * 
    * Use case: Student opts back into transport/hostel
    * 
    * Example: PUT /api/v1/accounts/student-fee-config/123/fee-type/TRANSPORT/activate?academicYearId=1
    */
   @PutMapping("/{studentId}/fee-type/{feeType}/activate")
   @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
   @Operation(summary = "Activate a fee type for a student")
   public ResponseEntity<Void> activateFeeType(
         @PathVariable Long studentId,
         @PathVariable FeeType feeType,
         @RequestParam Long academicYearId,
         Authentication auth) {
      
      log.info("Activating fee type {} for student {} - User: {}", 
         feeType, studentId, auth.getName());
      
      feeConfigService.activateFeeType(studentId, academicYearId, feeType);
      
      return ResponseEntity.ok().build();
   }
}
