package com.portal.studymate.auth.service.impl;

import com.portal.studymate.auth.dtos.ChangePasswordRequest;
import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.ResetPasswordRequest;
import com.portal.studymate.auth.dtos.SignupRequest;
import com.portal.studymate.auth.exception.InvalidCredentialsException;
import com.portal.studymate.auth.exception.InvalidTokenException;
import com.portal.studymate.auth.exception.UserAlreadyExistsException;
import com.portal.studymate.auth.exception.UserNotFoundException;
import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.auth.service.RateLimitService;
import com.portal.studymate.auth.service.TokenBlacklistService;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.common.jwt.JwtUtil;
import com.portal.studymate.common.util.HashUtil;
import com.portal.studymate.common.util.PasswordGenerator;
import com.portal.studymate.common.util.Role;
import com.portal.studymate.auth.model.User;
import com.portal.studymate.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

   private static final String USER_ROLE = "USER";
   private final UserRepository userRepository;
   private final JwtUtil jwtUtil;
   private final RedisTemplate<String, String> redisTemplate;
   private final PasswordEncoder passwordEncoder;
   private final RateLimitService rateLimitService;
   private final TokenBlacklistService tokenBlacklistService;
   private final JwtContextService jwtContextService;

   @Override
   public void signup(SignupRequest request) {

      if (userRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
         throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
      }

      if (request.getRole() == Role.ADMIN) {
         throw new IllegalArgumentException("Admin schoolmodule cannot be created via signup");
      }
      String schoolId = jwtContextService.getSchoolId();
      String tempPassword = PasswordGenerator.generate();

      User user = User.builder()
                      .email(request.getEmail().toLowerCase())
                      .password(passwordEncoder.encode(tempPassword))
                      .role(request.getRole())
                      .schoolId(schoolId)
                      .enabled(true)
                      .forcePasswordChange(true)
                      .build();

      userRepository.save(user);
      // 6. Send credentials (email / admin UI / log for dev)
//      notificationService.sendUserCredentials(
//         request.getEmail(),
//         tempPassword
//      );
      log.info(
         "TEMP PASSWORD for {} is {} (remove in production)",
         request.getEmail(),
         tempPassword
      );
      log.info(
         "User created successfully. Email={}, Role={}, School={}",
         request.getEmail(),
         request.getRole(),
         schoolId
      );
   }

   @Override
   public LoginResponse login(LoginRequest loginRequest) {
      User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

      if (!user.isEnabled()) {
         throw new IllegalStateException("User account is disabled");
      }

      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
         throw new InvalidCredentialsException("Invalid email or password");
      }

      String accessToken = jwtUtil.generateToken(user);
      String refreshToken = jwtUtil.generateRefreshToken(user);

      storeRefreshToken(refreshToken, user.getId());

      log.info("User logged in successfully: {}", user.getEmail());

      return LoginResponse.builder()
                          .token(accessToken)
                          .refreshToken(refreshToken)
                          .role(user.getRole())
                          .schoolId(user.getSchoolId())
                          .forcePasswordChange(user.isForcePasswordChange())
                          .message(user.isForcePasswordChange()
                                   ? "Password change required"
                                   : "Login successful")
                          .build();
   }

   @Override
   public void changePassword(ChangePasswordRequest request) {
      // 1. Get current userId from JWT
      String userId = jwtContextService.getUserId();
      User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

      // 3. Verify old password
      if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
         throw new InvalidCredentialsException("Old password is incorrect");
      }

      // 4. Prevent password reuse (optional but good)
      if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
         throw new IllegalArgumentException("New password cannot be same as old password");
      }
      // 5. Update password
      user.setPassword(passwordEncoder.encode(request.getNewPassword()));

      // 6. Clear forcePasswordChange flag
      user.setForcePasswordChange(false);

      userRepository.save(user);

      log.info("Password changed successfully for userId={}", userId);
   }

   @Override
   public LoginResponse generateRefreshToken(String refreshToken,String ip) {

      Claims claims;
      try {
         claims = jwtUtil.parseToken(refreshToken);
      }
      catch (Exception e) {
         throw new InvalidTokenException("Invalid refresh token");
      }
      String userIdFromToken = claims.getSubject();
      String hashed = HashUtil.sha256(refreshToken);
      String key = "refresh:" + hashed;
      String userId = redisTemplate.opsForValue().get(key);

      if (userId == null) {
         throw new InvalidTokenException("Invalid or expired refresh token");
      }

      if (!rateLimitService.isAllowed("refresh:schoolmodule:" + userId + ":ip:" + ip, 10, Duration.ofMinutes(1))) {
         return LoginResponse.builder().message("Too many refresh attempts.").build();
      }

      redisTemplate.delete(key);

      User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));
      if (!user.isEnabled()) {
         throw new IllegalStateException("User account is disabled");
      }

      String newAccessToken = jwtUtil.generateToken(user);
      String newRefreshToken = jwtUtil.generateRefreshToken(user);

      storeRefreshToken(newRefreshToken, userId);
      log.info("Tokens refreshed for schoolmodule: {}", user.getEmail());
      return LoginResponse.builder()
                          .token(newAccessToken)
                          .refreshToken(newRefreshToken)
                          .role(user.getRole())
                          .schoolId(user.getSchoolId())
                          .forcePasswordChange(user.isForcePasswordChange())
                          .message("Success")
                          .build();
   }

   @Override
   public void logout(String accessToken, String refreshToken) {

      if (accessToken != null && !accessToken.isBlank()) {
         long ttl = jwtUtil.getRemainingValidity(accessToken);

         if (ttl > 0) {
            tokenBlacklistService.blacklistToken(accessToken, ttl);
         }
      }

      if (refreshToken != null && !refreshToken.isBlank()) {
         redisTemplate.delete("refresh:" + HashUtil.sha256(refreshToken));
         // optional: short blacklist to prevent race reuse
         redisTemplate.opsForValue().set(
            "blacklist:refresh:" + HashUtil.sha256(refreshToken),
            "true",
            1,
            TimeUnit.MINUTES
         );
      }
   }

   private void storeRefreshToken(String token, String userId) {
      redisTemplate.opsForValue().set(
         "refresh:" + HashUtil.sha256(token),
         userId,
         7,
         TimeUnit.DAYS
      );
   }

   @Override
   public void forgotPassword(String email) {

      userRepository.findByEmail(email.toLowerCase()).ifPresent(user -> {

         String rawToken = UUID.randomUUID().toString();
         String hashedToken = HashUtil.sha256(rawToken);

         String key = "pwd-reset:" + hashedToken;

         redisTemplate.opsForValue().set(
            key,
            user.getId(),
            15,
            TimeUnit.MINUTES
         );

         // 🔔 Stub (replace with real email later)
         log.info("Password reset token for {} : {}", email, rawToken);

         // Later:
         // notificationService.sendPasswordReset(email, rawToken);
      });
   }

   @Override
   public void resetPassword(ResetPasswordRequest request) {

      String hashed = HashUtil.sha256(request.getToken());
      String key = "pwd-reset:" + hashed;

      String userId = redisTemplate.opsForValue().get(key);
      if (userId == null) {
         throw new InvalidTokenException("Invalid or expired reset token");
      }

      User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      user.setForcePasswordChange(false); // important
      userRepository.save(user);

      redisTemplate.delete(key);

      log.info("Password reset completed for schoolmodule {}", user.getEmail());
   }

   @Override
   public String createTeacherUser(
      String email,
      String fullName,
      String phone,
      String schoolId
   ) {

      // 1. Email already exists?
      if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
         throw new UserAlreadyExistsException("User already exists with email " + email);
      }

      // 2. Generate temp password
      String tempPassword = PasswordGenerator.generate();

      // 3. Create user (Mongo)
      User user = User.builder()
                      .email(email.toLowerCase())
                      .password(passwordEncoder.encode(tempPassword))
                      .role(Role.TEACHER)
                      .fullName(fullName)
                      .phoneNumber(phone)
                      .schoolId(schoolId)
                      .enabled(true)
                      .forcePasswordChange(true)
                      .build();

      userRepository.save(user);

      // 4. Notify teacher
//      notificationService.sendUserCredentials(
//         email,
//         tempPassword
//      );

      log.info("Teacher login created for {} in school {}", email, schoolId);
      return user.getId();
   }


}
