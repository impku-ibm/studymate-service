package com.portal.studymate.student.service;

import com.portal.studymate.student.dto.EnrollStudentRequest;
import com.portal.studymate.student.dto.StudentEnrollmentResponse;

import java.util.List;

public interface StudentEnrollmentService {
   StudentEnrollmentResponse enroll(EnrollStudentRequest request);
   List<StudentEnrollmentResponse> listActiveYear();
}
