package com.portal.studymate.teachermgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherRequest {

   @NotBlank
   private String fullName;

   @Email
   @NotBlank
   private String email;

   private String mobileNumber;

   private String qualification;

   private String notes;
}

