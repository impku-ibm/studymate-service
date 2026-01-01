package com.portal.studymate.academicyear.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcademicYearResponse {

   private Long id;
   private String year;     // "2024-2025"
   private String status;   // ACTIVE / COMPLETED
}
