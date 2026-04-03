package com.portal.studymate.auth.service;

import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.SignupRequest;
import com.portal.studymate.auth.exception.InvalidCredentialsException;
import com.portal.studymate.auth.exception.UserAlreadyExistsException;
import com.portal.studymate.auth.model.User;
import com.portal.studymate.auth.repository.UserRepository;
import com.portal.studymate.auth.service.impl.AuthServiceImpl;
import com.portal.studymate.common.jwt.JwtContextService;
import com.portal.studymate.common.jwt.JwtUtil;
import com.portal.studymate.common.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RateLimitService rateLimitService;
    @Mock private TokenBlacklistService tokenBlacklistService;
    @Mock private JwtContextService jwtContextService;
    @Mock private ValueOperations<String, String> valueOperations;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void login_success() {
        User user = User.builder()
            .id("user1").email("test@test.com").password("encoded")
            .role(Role.ADMIN).schoolId("SCH1").enabled(true).forcePasswordChange(false).build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(user)).thenReturn("refresh-token");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        LoginResponse result = authService.login(request);
        assertNotNull(result);
        assertEquals("access-token", result.getToken());
    }

    @Test
    void login_invalidCredentials_throws() {
        User user = User.builder()
            .id("user1").email("test@test.com").password("encoded")
            .role(Role.ADMIN).enabled(true).build();

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrong");

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_userNotFound_throws() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");
        request.setPassword("password");

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void signup_duplicateEmail_throws() {
        User existing = User.builder().id("u1").email("dup@test.com").build();
        when(userRepository.findByEmail("dup@test.com")).thenReturn(Optional.of(existing));

        SignupRequest request = new SignupRequest();
        request.setEmail("dup@test.com");
        request.setRole(Role.TEACHER);

        assertThrows(UserAlreadyExistsException.class, () -> authService.signup(request));
    }
}
