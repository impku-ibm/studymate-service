package com.portal.studymate.auth.controller;

import com.portal.studymate.auth.dtos.SignupRequest;
import com.portal.studymate.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

   private final AuthService authService;

   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping
   public ResponseEntity<String> createUser(@Valid @RequestBody SignupRequest req) {

      authService.signup(req);  // reuse existing logic

      return ResponseEntity.ok("User created successfully");
   }
}

