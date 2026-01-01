package com.portal.studymate.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequest {

   @NotBlank
   private String fullName;

   private LocalDate dateOfBirth;

   @NotNull
   private LocalDate admissionDate;

   @NotBlank
   private String parentName;

   @NotBlank
   private String parentMobile;

   @NotBlank
   private String address;
}

