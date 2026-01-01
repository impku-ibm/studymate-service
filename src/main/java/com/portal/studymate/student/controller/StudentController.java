package com.portal.studymate.student.controller;

import com.portal.studymate.student.dto.CreateStudentRequest;
import com.portal.studymate.student.dto.StudentResponse;
import com.portal.studymate.student.dto.UpdateStudentRequest;
import com.portal.studymate.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

   private final StudentService studentService;

   @PostMapping
   public StudentResponse create(@RequestBody @Valid CreateStudentRequest req) {
      return studentService.createStudent(req);
   }

   @GetMapping
   public List<StudentResponse> list() {
      return studentService.getAllStudents();
   }

   @PutMapping("/{id}")
   public StudentResponse update(
      @PathVariable Long id,
      @RequestBody @Valid UpdateStudentRequest req) {
      return studentService.updateStudent(id, req);
   }
}

