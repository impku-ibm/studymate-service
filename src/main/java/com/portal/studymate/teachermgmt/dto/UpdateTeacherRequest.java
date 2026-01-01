package com.portal.studymate.teachermgmt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTeacherRequest {

   @NotBlank
   private String fullName;

   private String mobileNumber;

   private String qualification;

   private String notes;

   private boolean active;
}

