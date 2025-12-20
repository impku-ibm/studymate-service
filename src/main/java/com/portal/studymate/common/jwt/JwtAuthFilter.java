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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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
      if (header != null && header.startsWith("Bearer ")) {
         String token = header.substring(7);

         try {
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated");
               return;
            }
            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);
            String schoolId = claims.get("schoolId", String.class);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims,
                                                                                                         null,
                                                                                                         List.of(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
         }
         catch (Exception e) {
            log.warn("JWT authentication failed: {}", e.getMessage());
         }
      }
      filterChain.doFilter(request, response);
   }
}
