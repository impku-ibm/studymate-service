package com.portal.studymate.student.service;

import com.portal.studymate.student.dto.CreateStudentRequest;
import com.portal.studymate.student.dto.StudentResponse;
import com.portal.studymate.student.dto.UpdateStudentRequest;

import java.util.List;

public interface StudentService {

   StudentResponse createStudent(CreateStudentRequest request);

   List<StudentResponse> getAllStudents();

   StudentResponse updateStudent(Long id, UpdateStudentRequest request);
}

