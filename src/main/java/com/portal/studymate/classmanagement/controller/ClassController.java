package com.portal.studymate.classmanagement.controller;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;
import com.portal.studymate.classmanagement.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {

   private final ClassService classService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public ClassResponse createClass(
      @Valid @RequestBody CreateClassRequest request) {
      return classService.createClass(request);
   }

   @GetMapping
   public List<ClassResponse> getAllClasses() {
      return classService.getAllClasses();
   }

   @PutMapping("/{classId}")
   public ClassResponse updateClass(
      @PathVariable Long classId,
      @Valid @RequestBody UpdateClassRequest request) {
      return classService.updateClass(classId, request);
   }
}
