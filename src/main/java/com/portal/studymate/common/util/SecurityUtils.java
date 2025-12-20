package com.portal.studymate.common.util;


import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {
   private SecurityUtils() {}

   public static UUID getSchoolId() {
      Authentication auth = SecurityContextHolder
                               .getContext()
                               .getAuthentication();

      Claims claims = (Claims) auth.getPrincipal();
      return UUID.fromString(claims.get("schoolId", String.class));
   }

   public static String getRole() {
      Claims claims = (Claims) SecurityContextHolder
                                  .getContext()
                                  .getAuthentication()
                                  .getPrincipal();

      return claims.get("role", String.class);
   }

   public static UUID getUserId() {
      Claims claims = (Claims) SecurityContextHolder
                                  .getContext()
                                  .getAuthentication()
                                  .getPrincipal();

      return UUID.fromString(claims.get("userId", String.class));
   }
}
