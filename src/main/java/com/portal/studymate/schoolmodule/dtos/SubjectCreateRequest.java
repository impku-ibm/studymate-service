package com.portal.studymate.schoolmodule.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectCreateRequest {

   @NotBlank
   private String name;

   private String code;
   private String category;
}
