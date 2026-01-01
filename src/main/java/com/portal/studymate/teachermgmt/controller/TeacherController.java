package com.portal.studymate.teachermgmt.controller;

import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.dto.UpdateTeacherRequest;
import com.portal.studymate.teachermgmt.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

   private final TeacherService teacherService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public TeacherResponse createTeacher(
      @Valid @RequestBody CreateTeacherRequest request) {
      return teacherService.createTeacher(request);
   }

   @GetMapping
   public List<TeacherResponse> getAllTeachers() {
      return teacherService.getAllTeachers();
   }

   @PutMapping("/{teacherId}")
   public TeacherResponse updateTeacher(
      @PathVariable Long teacherId,
      @Valid @RequestBody UpdateTeacherRequest request) {
      return teacherService.updateTeacher(teacherId, request);
   }
}

