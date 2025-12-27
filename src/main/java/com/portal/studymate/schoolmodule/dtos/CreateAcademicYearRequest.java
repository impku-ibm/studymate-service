package com.portal.studymate.schoolmodule.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAcademicYearRequest {
   @NotBlank
   private String year;   // 2024-25

   private LocalDate startDate;
   private LocalDate endDate;
}
