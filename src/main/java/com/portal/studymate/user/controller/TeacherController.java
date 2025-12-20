package com.portal.studymate.user.controller;

import com.portal.studymate.user.dtos.AssignTeacherRequest;
import com.portal.studymate.user.dtos.CreateTeacherRequest;
import com.portal.studymate.user.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/teachers")
@Slf4j
public class TeacherController {
   private final TeacherService service;

   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping("/{schoolId}")
   public ResponseEntity<?> addTeacher(
      @PathVariable UUID schoolId,
      @Valid @RequestBody CreateTeacherRequest req,
      Authentication auth
   ) {
      log.info("Adding teacher - User: {}, SchoolId: {}, Teacher: {}", 
               auth.getName(), schoolId, req.name());
      try {
         var result = service.addTeacher(schoolId, req);
         log.info("Teacher added successfully - SchoolId: {}, Teacher: {}", 
                  schoolId, req.name());
         return ResponseEntity.ok(result);
      } catch (Exception e) {
         log.error("Failed to add teacher - User: {}, SchoolId: {}, Error: {}", 
                   auth.getName(), schoolId, e.getMessage(), e);
         throw e;
      }
   }


   @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL')")
   @PostMapping("/assign")
   public ResponseEntity<?> assignTeacher(
      @Valid @RequestBody AssignTeacherRequest req,
      Authentication auth
   ) {
      log.info("Assigning teacher - User: {}, TeacherId: {}, ClassRoomId: {}, SubjectId: {}", 
               auth.getName(), req.teacherId(), req.classroomId(), req.subjectId());
      try {
         var result = service.assign(req);
         log.info("Teacher assigned successfully - TeacherId: {}, ClassRoomId: {}, SubjectId: {}", 
                  req.teacherId(), req.classroomId(), req.subjectId());
         return ResponseEntity.ok(result);
      } catch (Exception e) {
         log.error("Failed to assign teacher - User: {}, TeacherId: {}, Error: {}", 
                   auth.getName(), req.teacherId(), e.getMessage(), e);
         throw e;
      }
   }
}
