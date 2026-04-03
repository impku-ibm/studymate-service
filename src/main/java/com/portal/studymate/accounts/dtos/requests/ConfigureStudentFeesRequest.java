package com.portal.studymate.accounts.dtos.requests;

import com.portal.studymate.accounts.enums.FeeType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request to configure which fee types are applicable for a student.
 * 
 * Used in two scenarios:
 * 1. During admission - to set initial fee types for the student
 * 2. During academic year - to add/remove fee types (e.g., opt out of transport)
 * 
 * Example:
 * {
 *   "academicYearId": 1,
 *   "feeTypes": ["TUITION", "TRANSPORT", "EXAM"]
 * }
 */
public record ConfigureStudentFeesRequest(
   
   // Academic year for which fees are being configured
   @NotNull(message = "Academic year ID is required")
   Long academicYearId,
   
   // List of fee types applicable for this student
   // At least one fee type must be selected (typically TUITION is mandatory)
   @NotEmpty(message = "At least one fee type must be selected")
   List<FeeType> feeTypes
) {}
