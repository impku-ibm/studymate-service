package com.portal.studymate.auth.service;

import com.portal.studymate.auth.dtos.ChangePasswordRequest;
import com.portal.studymate.auth.dtos.LoginRequest;
import com.portal.studymate.auth.dtos.LoginResponse;
import com.portal.studymate.auth.dtos.ResetPasswordRequest;
import com.portal.studymate.auth.dtos.SignupRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
   void signup(SignupRequest signupRequest);

   LoginResponse login(LoginRequest loginRequest);

   LoginResponse generateRefreshToken(String refreshToken,String ip);
   void logout(String accessToken, String refreshToken);
   void changePassword(ChangePasswordRequest request);

   void forgotPassword(@Email @NotBlank String email);

   void resetPassword(@Valid ResetPasswordRequest request);

   String createTeacherUser(String email, String fullName, String phone, String schoolId);
}
