package com.portal.studymate.subject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubjectRequest {

   @NotBlank
   private String name;

   @NotBlank
   private String code;   // e.g. MATH, ENG
}
