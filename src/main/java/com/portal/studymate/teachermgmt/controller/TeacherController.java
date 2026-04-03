package com.portal.studymate.teachermgmt.controller;

import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.dto.UpdateTeacherRequest;
import com.portal.studymate.teachermgmt.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

   private final TeacherService teacherService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public TeacherResponse createTeacher(
      @Valid @RequestBody CreateTeacherRequest request) {
      log.info("POST /teachers - Creating teacher");
      return teacherService.createTeacher(request);
   }

   @GetMapping
   public List<TeacherResponse> getAllTeachers() {
      log.info("GET /teachers - Getting all teachers");
      return teacherService.getAllTeachers();
   }

   @PutMapping("/{teacherId}")
   public TeacherResponse updateTeacher(
      @PathVariable Long teacherId,
      @Valid @RequestBody UpdateTeacherRequest request) {
      log.info("PUT /teachers/{} - Updating teacher", teacherId);
      return teacherService.updateTeacher(teacherId, request);
   }
}

