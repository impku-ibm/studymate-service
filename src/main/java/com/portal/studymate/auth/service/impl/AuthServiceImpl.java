package com.portal.studymate.auth.service.impl;

import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.SignupRequest;
import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.auth.service.RateLimitService;
import com.portal.studymate.auth.service.TokenBlacklistService;
import com.portal.studymate.common.exception.InvalidCredentialsException;
import com.portal.studymate.common.exception.InvalidTokenException;
import com.portal.studymate.common.exception.UserAlreadyExistsException;
import com.portal.studymate.common.exception.UserNotFoundException;
import com.portal.studymate.common.jwt.JwtUtil;
import com.portal.studymate.common.util.HashUtil;
import com.portal.studymate.user.model.User;
import com.portal.studymate.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

   @Override
   public void signup(SignupRequest request) {
      if (userRepository.findByEmail(request.getEmail()).isPresent()) {
         throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
      }
      userRepository.save(User.builder()
                              .email(request.getEmail())
                              .password(passwordEncoder.encode(request.getPassword()))
                              .role(USER_ROLE)
                              .enabled(true)
                              .build());
      log.info("User registered successfully with email: {}", request.getEmail());
   }

   @Override
   public LoginResponse login(LoginRequest loginRequest) {
      User user = userRepository.findByEmail(loginRequest.getEmail())
                                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

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
                          .build();
   }

   public LoginResponse generateRefreshToken(String refreshToken,String ip) {
      String hashed = HashUtil.sha256(refreshToken);
      String key = "refresh:" + hashed;
      String userId = redisTemplate.opsForValue().get(key);

      if (userId == null) {
         throw new InvalidTokenException("Invalid or expired refresh token");
      }
      if (!rateLimitService.isAllowed("refresh:user:" + userId + ":ip:" + ip, 10, Duration.ofMinutes(1))) {
         return LoginResponse.builder().message("Too many refresh attempts.").build();
      }
      redisTemplate.delete(key);
      User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

      String newAccessToken = jwtUtil.generateToken(user);
      String newRefreshToken = jwtUtil.generateRefreshToken(user);
      storeRefreshToken(newRefreshToken, userId);
      log.info("Tokens refreshed for user: {}", user.getEmail());
      return LoginResponse.builder()
                          .token(newAccessToken)
                          .refreshToken(newRefreshToken)
                          .build();
   }

   @Override
   public void logout(String accessToken, String refreshToken) {

      long ttl = jwtUtil.getRemainingValidity(accessToken);
      tokenBlacklistService.blacklistToken(accessToken, ttl);

      if (refreshToken != null && !refreshToken.isBlank()) {
         redisTemplate.delete("refresh:" + HashUtil.sha256(refreshToken));
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

}
