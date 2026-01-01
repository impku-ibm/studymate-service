package com.portal.studymate.academicyear.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAcademicYearRequest {

   @NotNull
   @Min(2000)
   private Integer startYear;

   // e.g. 2024 → 2024-2025
}