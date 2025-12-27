package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.student.CreateStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.StudentResponse;
import com.portal.studymate.schoolmodule.dtos.student.UpdateStudentRequest;
import com.portal.studymate.schoolmodule.model.Student;

import java.util.List;

public interface StudentService {
   StudentResponse create(CreateStudentRequest req);
   StudentResponse update(Long studentId, UpdateStudentRequest req);
   StudentResponse getById(Long studentId);
   List<StudentResponse> list();
   void markLeft(Long studentId);
   Student get(Long studentId);
}
