package com.portal.studymate.accounts.dtos.responses;

import com.portal.studymate.accounts.enums.FeeType;

import java.time.LocalDate;

/**
 * Response showing a student's fee configuration.
 * Displays which fee types are applicable and their status.
 * 
 * Used in:
 * - Fee management UI to show student's active fees
 * - Admission screen to display configured fees
 * - Accountant dashboard to see fee breakdown
 */
public record StudentFeeConfigurationResponse(
   
   Long id,
   
   // Fee type (TUITION, TRANSPORT, HOSTEL, etc.)
   FeeType feeType,
   
   // Whether this fee is currently active
   boolean active,
   
   // When this fee configuration started
   LocalDate startDate,
   
   // When this fee configuration ended (null if ongoing)
   LocalDate endDate,
   
   // Human-readable status
   String status // "Active", "Inactive", "Ended"
) {
   
   /**
    * Helper method to determine status based on active flag and end date
    */
   public static String determineStatus(boolean active, LocalDate endDate) {
      if (!active) return "Inactive";
      if (endDate != null && endDate.isBefore(LocalDate.now())) return "Ended";
      return "Active";
   }
}
