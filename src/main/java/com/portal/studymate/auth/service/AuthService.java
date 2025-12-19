package com.portal.studymate.auth.service;

import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.SignupRequest;

public interface AuthService {
   void signup(SignupRequest signupRequest);

   LoginResponse login(LoginRequest loginRequest);

   LoginResponse generateRefreshToken(String refreshToken,String ip);
   void logout(String accessToken, String refreshToken);
}
