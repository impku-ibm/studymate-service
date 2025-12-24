package com.portal.studymate.common.jwt;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtContextService {

   private Authentication auth() {
      Authentication auth = SecurityContextHolder
                               .getContext()
                               .getAuthentication();

      if (auth == null || !auth.isAuthenticated()) {
         throw new IllegalStateException("No authenticated user");
      }
      return auth;
   }

   public String getUserId() {
      return auth().getName(); // principal
   }

   @SuppressWarnings("unchecked")
   private Map<String, Object> getDetails() {
      Object details = auth().getDetails();

      if (!(details instanceof Map)) {
         throw new IllegalStateException("Invalid authentication details");
      }
      return (Map<String, Object>) details;
   }

   public String getSchoolId() {
      return (String) getDetails().get("schoolId");
   }

   public String getRole() {
      return (String) getDetails().get("role");
   }
}
