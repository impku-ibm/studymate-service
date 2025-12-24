package com.portal.studymate.auth.dtos;

import com.portal.studymate.common.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LoginResponse {
   private String token;
   private String refreshToken;

   private Role role;
   private String schoolId;

   private boolean forcePasswordChange;

   private String message;
}
