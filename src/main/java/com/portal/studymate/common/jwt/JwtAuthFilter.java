package com.portal.studymate.common.jwt;

import com.portal.studymate.auth.service.TokenBlacklistService;
import com.portal.studymate.common.util.HashUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
         String schoolId = claims.get("schoolId", String.class);
         if (userId == null || role == null || schoolId == null) {
            throw new IllegalStateException("Invalid JWT claims");
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
         details.put("schoolId", schoolId);
         details.put("role", role);

         authentication.setDetails(details);
         SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      catch (Exception ex) {
         log.warn("JWT authentication failed: {}", ex.getMessage());
      }
      filterChain.doFilter(request, response);
   }
}
