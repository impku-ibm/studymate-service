package com.portal.studymate.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {
   /**
    * Refresh token to invalidate server-side
    */
   @NotBlank(message = "Refresh token is required")
   private String refreshToken;
}
