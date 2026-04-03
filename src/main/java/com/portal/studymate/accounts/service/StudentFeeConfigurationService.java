package com.portal.studymate.accounts.service;

import com.portal.studymate.accounts.dtos.requests.ConfigureStudentFeesRequest;
import com.portal.studymate.accounts.dtos.responses.StudentFeeConfigurationResponse;
import com.portal.studymate.accounts.enums.FeeType;

import java.util.List;

/**
 * Service for managing student fee configurations.
 * 
 * Handles:
 * - Configuring which fee types apply to each student
 * - Activating/deactivating fee types (e.g., student opts out of transport)
 * - Retrieving student's active fee configurations
 */
public interface StudentFeeConfigurationService {

   /**
    * Configure fee types for a student.
    * This will:
    * 1. Add new fee types that don't exist
    * 2. Activate fee types that were previously inactive
    * 3. Deactivate fee types that are not in the request
    * 
    * Used during admission or when updating student's fee structure.
    * 
    * @param studentId The student ID
    * @param request The fee configuration request
    * @return List of configured fee types
    */
   List<StudentFeeConfigurationResponse> configureStudentFees(Long studentId, ConfigureStudentFeesRequest request);

   /**
    * Get all fee configurations for a student (active and inactive).
    * Used in fee management UI to show complete fee history.
    * 
    * @param studentId The student ID
    * @param academicYearId The academic year ID
    * @return List of fee configurations
    */
   List<StudentFeeConfigurationResponse> getStudentFeeConfigurations(Long studentId, Long academicYearId);

   /**
    * Deactivate a specific fee type for a student.
    * Used when student opts out of a fee (e.g., stops using transport).
    * 
    * @param studentId The student ID
    * @param academicYearId The academic year ID
    * @param feeType The fee type to deactivate
    */
   void deactivateFeeType(Long studentId, Long academicYearId, FeeType feeType);

   /**
    * Activate a previously deactivated fee type.
    * Used when student opts back in (e.g., starts using transport again).
    * 
    * @param studentId The student ID
    * @param academicYearId The academic year ID
    * @param feeType The fee type to activate
    */
   void activateFeeType(Long studentId, Long academicYearId, FeeType feeType);
}
