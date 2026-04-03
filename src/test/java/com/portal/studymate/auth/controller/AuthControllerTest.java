package com.portal.studymate.auth.controller;

import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.service.AuthService;
import com.portal.studymate.auth.service.RateLimitService;
import com.portal.studymate.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock private AuthService authService;
    @Mock private RateLimitService rateLimitService;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthController controller;

    @Test
    void login_success_returnsOk() {
        var response = LoginResponse.builder().token("jwt-token").refreshToken("refresh").build();
        when(rateLimitService.isAllowed(anyString(), anyInt(), any())).thenReturn(true);
        when(authService.login(any())).thenReturn(response);

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("password");

        var result = controller.login(request, servletRequest);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwt-token", result.getBody().getToken());
    }

    @Test
    void login_rateLimited_returns429() {
        when(rateLimitService.isAllowed(anyString(), anyInt(), any())).thenReturn(false);

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("password");

        var result = controller.login(request, servletRequest);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, result.getStatusCode());
    }
}
