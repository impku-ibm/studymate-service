package com.portal.studymate.user.controller;

import com.portal.studymate.user.dtos.CreateStudentRequest;
import com.portal.studymate.user.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

   private final StudentService service;

   @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','TEACHER')")
   @PostMapping("/{schoolId}")
   public ResponseEntity<?> addStudent(
      @PathVariable UUID schoolId,
      @Valid @RequestBody CreateStudentRequest req,
      Authentication auth
   ) {
      log.info("Adding student - User: {}, SchoolId: {}, Student: {}, ClassRoomId: {}", 
               auth.getName(), schoolId, req.name(), req.classroomId());
      try {
         var result = service.addStudent(schoolId, req);
         log.info("Student added successfully - SchoolId: {}, Student: {}", 
                  schoolId, req.name());
         return ResponseEntity.ok(result);
      } catch (Exception e) {
         log.error("Failed to add student - User: {}, SchoolId: {}, Error: {}", 
                   auth.getName(), schoolId, e.getMessage(), e);
         throw e;
      }
   }

   @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','TEACHER','STUDENT')")
   @GetMapping("/class/{classroomId}")
   public ResponseEntity<?> getByClass(
      @PathVariable UUID classroomId,
      Authentication auth
   ) {
      log.info("Getting students by class - User: {}, ClassroomId: {}", 
               auth.getName(), classroomId);
      try {
         var result = service.getByClass(classroomId);
         log.info("Retrieved students successfully - ClassroomId: {}, Count: {}", 
                  classroomId, result instanceof java.util.List ? ((java.util.List<?>) result).size() : "N/A");
         return ResponseEntity.ok(result);
      } catch (Exception e) {
         log.error("Failed to get students by class - User: {}, ClassroomId: {}, Error: {}", 
                   auth.getName(), classroomId, e.getMessage(), e);
         throw e;
      }
   }
}
