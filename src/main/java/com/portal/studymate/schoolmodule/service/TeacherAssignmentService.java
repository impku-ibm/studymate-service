package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.teacher.AssignTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherAssignmentResponse;

import java.util.List;

public interface TeacherAssignmentService {
   void assignTeacher(AssignTeacherRequest request);

   List<TeacherAssignmentResponse> getAssignments(Long academicYearId);

   void removeAssignment(Long assignmentId);
}
