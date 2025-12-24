package com.portal.studymate.auth.dtos;

import com.portal.studymate.common.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupRequest {
   @NotBlank
   @Email
   private String email;          // username / login id

   @NotNull
   private Role role;             // TEACHER / STUDENT / STAFF

   @NotBlank
   private String fullName;        // display name (basic identity)

   private String phoneNumber;
}
