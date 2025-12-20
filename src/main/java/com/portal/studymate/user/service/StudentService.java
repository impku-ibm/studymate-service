package com.portal.studymate.user.service;

import com.portal.studymate.user.dtos.CreateStudentRequest;
import com.portal.studymate.user.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
   Student addStudent(UUID schoolId, CreateStudentRequest req);
   List<Student> getByClass(UUID classroomId);
}
