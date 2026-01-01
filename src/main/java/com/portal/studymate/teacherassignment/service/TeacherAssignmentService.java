package com.portal.studymate.teacherassignment.service;

import com.portal.studymate.teacherassignment.dto.CreateTeacherAssignmentRequest;
import com.portal.studymate.teacherassignment.dto.TeacherAssignmentResponse;

import java.util.List;

public interface TeacherAssignmentService {

   TeacherAssignmentResponse assignTeacher(
      CreateTeacherAssignmentRequest request
   );

   List<TeacherAssignmentResponse> getAssignmentsForSection(Long sectionId);

   List<TeacherAssignmentResponse> getAssignmentsForTeacher();
}
