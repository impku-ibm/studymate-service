package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.student.ChangeSectionRequest;
import com.portal.studymate.schoolmodule.dtos.student.EnrollStudentRequest;
import com.portal.studymate.schoolmodule.dtos.student.PromoteStudentRequest;

public interface StudentEnrollmentService {
   void enroll(EnrollStudentRequest req);
   void changeSection(Long enrollmentId, ChangeSectionRequest req);
   void promote(Long enrollmentId, PromoteStudentRequest req);
}
