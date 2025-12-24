package com.portal.studymate.auth.controller;

import com.portal.studymate.auth.dtos.ChangePasswordRequest;
import com.portal.studymate.auth.dtos.ForgotPasswordRequest;
import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.LogoutRequest;
import com.portal.studymate.auth.dtos.RefreshTokenRequest;
import com.portal.studymate.auth.dtos.ResetPasswordRequest;
import com.portal.studymate.auth.dtos.SignupRequest;
import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.auth.service.RateLimitService;
import com.portal.studymate.common.exception.InvalidTokenException;
import com.portal.studymate.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
   private final AuthService authService;
   private final RateLimitService rateLimitService;
   private final JwtUtil jwtUtil;

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest servletRequest) {
      String ipAddress = servletRequest.getRemoteAddr();
      if (!rateLimitService.isAllowed("login:ip:" + ipAddress, 5, Duration.ofMinutes(1))) {
         return ResponseEntity
                   .status(HttpStatus.TOO_MANY_REQUESTS)
                   .body(LoginResponse.builder().message("Too many login attempts. Please try again later.").build());
      }
      req.setIpAddress(ipAddress);
      return ResponseEntity.ok(authService.login(req));
   }

   @PreAuthorize("isAuthenticated()")
   @PostMapping("/change-password")
   public ResponseEntity<String> changePassword(
      @Valid @RequestBody ChangePasswordRequest request) {

      authService.changePassword(request);
      return ResponseEntity.ok("Password changed successfully");
   }


   @PostMapping("/logout")
   public ResponseEntity<String> logout(
      @RequestHeader(value = "Authorization" , required = false) String authHeader ,
      @Valid @RequestBody LogoutRequest request) {

      if (!authHeader.startsWith("Bearer ")) {
         throw new InvalidTokenException("Invalid authorization header");
      }

      String token = authHeader.substring(7);

      authService.logout(
         authHeader.substring(7),
         request.getRefreshToken()
      );
      return ResponseEntity.ok("Logged out successfully");
   }

   @PostMapping("/refresh-token")
   public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request,HttpServletRequest servletRequest) {
      String ip = servletRequest.getRemoteAddr();
      if (!rateLimitService.isAllowed(
         "refresh:ip:" + ip,
         10,
         Duration.ofMinutes(1))) {

         return ResponseEntity
                   .status(HttpStatus.TOO_MANY_REQUESTS)
                   .body(LoginResponse.builder()
                                      .message("Too many refresh attempts. Try again later.")
                                      .build());
      }
      return ResponseEntity.ok(
         authService.generateRefreshToken(request.getRefreshToken(), ip)
      );
   }

   @PostMapping("/forgot-password")
   public ResponseEntity<String> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequest request) {

      authService.forgotPassword(request.getEmail());
      return ResponseEntity.ok(
         "If the email exists, password reset instructions have been sent"
      );
   }
   @PostMapping("/reset-password")
   public ResponseEntity<String> resetPassword(
      @Valid @RequestBody ResetPasswordRequest request) {

      authService.resetPassword(request);
      return ResponseEntity.ok("Password reset successful");
   }


}
