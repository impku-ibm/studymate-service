package com.portal.studymate.schoolmodule.dtos.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTeacherRequest {
   @NotBlank
   private String fullName;

   @Email @NotBlank
   private String email;

   @NotBlank
   private String mobileNumber;

   @NotBlank
   private String qualification;

   private String notes;
}
