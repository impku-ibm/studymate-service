package com.portal.studymate.schoolmodule.controller;

import com.portal.studymate.schoolmodule.dtos.teacher.CreateTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherResponse;
import com.portal.studymate.schoolmodule.dtos.teacher.UpdateTeacherRequest;
import com.portal.studymate.schoolmodule.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TeacherController {
   private final TeacherService teacherService;

   @PostMapping
   public ResponseEntity<Void> createTeacher(
      @Valid @RequestBody CreateTeacherRequest request) {

      teacherService.createTeacher(request);
      return ResponseEntity.ok().build();
   }

   @GetMapping
   public List<TeacherResponse> getTeachers() {
      return teacherService.getAllTeachers();
   }

   @GetMapping(("/{id}"))
   public TeacherResponse getTeacherById(Long id) {
      return teacherService.getTeacherById(id);
   }

   @PutMapping("/{id}")
   public void updateTeacher(
      @PathVariable Long id,
      @RequestBody UpdateTeacherRequest request) {
      teacherService.updateTeacher(id, request);
   }

   @DeleteMapping("/{id}")
   public void deactivateTeacher(@PathVariable Long id) {
      teacherService.deactivateTeacher(id);
   }
}
