package com.portal.studymate.common.jwt;

import com.portal.studymate.auth.service.TokenBlacklistService;
import com.portal.studymate.common.context.SchoolContext;
import com.portal.studymate.common.exception.ResourceNotFoundException;
import com.portal.studymate.common.util.HashUtil;
import com.portal.studymate.school.model.School;
import com.portal.studymate.school.repository.SchoolRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

   private final JwtUtil jwtUtil;
   private final TokenBlacklistService tokenBlacklistService;
   private final SchoolRepository schoolRepository;

   @Override
   protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {

      String header = request.getHeader("Authorization");

      if (header == null || !header.startsWith("Bearer ")) {
         filterChain.doFilter(request, response);
         return;
      }
      String token = header.substring(7);
      try {
         // 1. Blacklist check
         if (tokenBlacklistService.isTokenBlacklisted(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated");
            return;
         }

         // 2. Parse token
         Claims claims = jwtUtil.parseToken(token);

         String userId = claims.getSubject();               // sub
         String role = claims.get("role", String.class);
         String schoolCode = claims.get("schoolId", String.class);
         if (userId == null || role == null || schoolCode == null) {
            throw new IllegalStateException("Invalid JWT claims");
         }

         String path = request.getRequestURI();
         boolean isSchoolBootstrap =
            path.equals("/api/school") && request.getMethod().equals("POST");

         if (!isSchoolBootstrap) {
            if (schoolCode == null) {
               throw new IllegalStateException("School code missing in token");
            }

            School school = schoolRepository
                               .findBySchoolCodeAndActiveTrue(schoolCode)
                               .orElseThrow(() ->
                                               new ResourceNotFoundException(
                                                  "SCHOOL_NOT_FOUND",
                                                  "School not found for code: " + schoolCode
                                               )
                               );

            SchoolContext.setSchool(school);
         }


         // 3. Build authorities
         List<GrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role));
         // 4. Create authentication
         UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
               userId,          // PRINCIPAL
               null,
               authorities
            );
         Map<String, Object> details = new HashMap<>();
         details.put("schoolId", schoolCode);
         details.put("role", role);

         authentication.setDetails(details);
         SecurityContextHolder.getContext().setAuthentication(authentication);
         filterChain.doFilter(request, response);
      }
      catch (Exception ex) {
         log.warn("JWT authentication failed: {}", ex.getMessage());
      }finally {
         // 7. Clear context to avoid leaks
         SchoolContext.clear();
      }
   }
}
