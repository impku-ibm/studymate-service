package com.portal.studymate.schoolmodule.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubjectUpdateRequest {
   @NotBlank
   private String name;

   private String code;
   private String category;
   private Boolean active;
}
