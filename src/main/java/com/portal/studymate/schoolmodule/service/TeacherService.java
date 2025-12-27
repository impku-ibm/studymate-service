package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.teacher.CreateTeacherRequest;
import com.portal.studymate.schoolmodule.dtos.teacher.TeacherResponse;
import com.portal.studymate.schoolmodule.dtos.teacher.UpdateTeacherRequest;

import java.util.List;

public interface TeacherService {
   void createTeacher(CreateTeacherRequest request);
   // 📋 Teacher directory list
   List<TeacherResponse> getAllTeachers();
   // 👁️ Eye icon – view details
   TeacherResponse getTeacherById(Long teacherId);
   // ✏️ Pencil icon – edit profile
   void updateTeacher(Long teacherId, UpdateTeacherRequest request);
   // 🚫 Disable teacher
   void deactivateTeacher(Long teacherId);
}
