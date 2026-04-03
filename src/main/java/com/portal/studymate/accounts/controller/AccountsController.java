package com.portal.studymate.accounts.controller;

import com.portal.studymate.accounts.dtos.requests.CreateFeeStructureRequest;
import com.portal.studymate.accounts.dtos.requests.RecordPaymentRequest;
import com.portal.studymate.accounts.dtos.responses.AccountsDashboardResponse;
import com.portal.studymate.accounts.dtos.responses.DailyCollectionReportResponse;
import com.portal.studymate.accounts.dtos.responses.FeeCollectionTrendResponse;
import com.portal.studymate.accounts.dtos.responses.FeeStructureResponse;
import com.portal.studymate.accounts.dtos.responses.OutstandingFeesReportResponse;
import com.portal.studymate.accounts.dtos.responses.PaymentResponse;
import com.portal.studymate.accounts.dtos.responses.StudentFeeResponse;
import com.portal.studymate.accounts.service.FeeStructureService;
import com.portal.studymate.accounts.service.PaymentService;
import com.portal.studymate.accounts.service.ReportsService;
import com.portal.studymate.accounts.service.StudentFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Accounts Management", description = "ERP Accounts and Fee Management APIs")
public class AccountsController {

    private final FeeStructureService feeStructureService;
    private final StudentFeeService studentFeeService;
    private final PaymentService paymentService;
    private final ReportsService reportsService;

    @PostMapping("/fee-structures")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Create fee structure")
    public ResponseEntity<FeeStructureResponse> createFeeStructure(
            @Valid @RequestBody CreateFeeStructureRequest request,
            Authentication auth) {
        log.info("Creating fee structure - User: {}, Class: {}, FeeType: {}", 
                auth.getName(), request.classId(), request.feeType());
        return ResponseEntity.ok(feeStructureService.createFeeStructure(request));
    }

    @GetMapping("/fee-structures")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT', 'TEACHER')")
    @Operation(summary = "Get fee structures")
    public ResponseEntity<Page<FeeStructureResponse>> getFeeStructures(
            @RequestParam Long academicYearId,
            Pageable pageable) {
        return ResponseEntity.ok(feeStructureService.getFeeStructures(academicYearId, pageable));
    }

    @PostMapping("/student-fees/generate/class")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Generate fees for entire class")
    public ResponseEntity<Void> generateFeesForClass(
            @RequestParam Long academicYearId,
            @RequestParam Long classId,
            Authentication auth) {
        log.info("Generating fees for class - User: {}, Class: {}", 
                auth.getName(), classId);
        studentFeeService.generateFeesForClass(academicYearId, classId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student-fees/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT', 'TEACHER', 'PARENT', 'STUDENT')")
    @Operation(summary = "Get student fees")
    public ResponseEntity<Page<StudentFeeResponse>> getStudentFees(
            @PathVariable Long studentId,
            Pageable pageable) {
        return ResponseEntity.ok(studentFeeService.getStudentFees(studentId, pageable));
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Record payment")
    public ResponseEntity<PaymentResponse> recordPayment(
            @Valid @RequestBody RecordPaymentRequest request,
            Authentication auth) {
        log.info("Recording payment - User: {}, Student: {}, Amount: {}", 
                auth.getName(), request.studentId(), request.getTotalAmount());
        return ResponseEntity.ok(paymentService.recordPayment(request));
    }

    @GetMapping("/payments/receipt/{receiptNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT', 'TEACHER', 'PARENT')")
    @Operation(summary = "Get payment by receipt number")
    public ResponseEntity<PaymentResponse> getPaymentByReceipt(@PathVariable String receiptNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByReceipt(receiptNumber));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get accounts dashboard")
    public ResponseEntity<AccountsDashboardResponse> getAccountsDashboard(
            @RequestParam Long academicYearId) {
        return ResponseEntity.ok(reportsService.getAccountsDashboard(academicYearId));
    }

    @GetMapping("/reports/daily-collection")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get daily collection report")
    public ResponseEntity<DailyCollectionReportResponse> getDailyCollectionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportsService.getDailyCollectionReport(date));
    }

    @GetMapping("/reports/outstanding-fees")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get outstanding fees report")
    public ResponseEntity<OutstandingFeesReportResponse> getOutstandingFeesReport(
            @RequestParam Long academicYearId) {
        return ResponseEntity.ok(reportsService.getOutstandingFeesReport(academicYearId));
    }

    @GetMapping("/analytics/collection-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get fee collection trend")
    public ResponseEntity<List<FeeCollectionTrendResponse>> getFeeCollectionTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportsService.getFeeCollectionTrend(startDate, endDate));
    }

    @PutMapping("/fee-structures/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Update fee structure")
    public ResponseEntity<FeeStructureResponse> updateFeeStructure(
            @PathVariable Long id,
            @Valid @RequestBody CreateFeeStructureRequest request) {
        return ResponseEntity.ok(feeStructureService.updateFeeStructure(id, request));
    }

    @DeleteMapping("/fee-structures/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Delete fee structure")
    public ResponseEntity<Void> deleteFeeStructure(@PathVariable Long id) {
        feeStructureService.deleteFeeStructure(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/fee-structures/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Toggle fee structure active status")
    public ResponseEntity<FeeStructureResponse> toggleFeeStructure(@PathVariable Long id) {
        return ResponseEntity.ok(feeStructureService.toggleFeeStructure(id));
    }

    @GetMapping("/student-fees/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRINCIPAL', 'ACCOUNTANT')")
    @Operation(summary = "Get student fees by class")
    public ResponseEntity<Page<StudentFeeResponse>> getStudentFeesByClass(
            @PathVariable Long classId,
            @RequestParam Long academicYearId,
            Pageable pageable) {
        return ResponseEntity.ok(studentFeeService.getStudentFeesByClass(classId, academicYearId, pageable));
    }
}