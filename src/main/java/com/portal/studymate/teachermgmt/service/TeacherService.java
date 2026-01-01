package com.portal.studymate.teachermgmt.service;
import com.portal.studymate.teachermgmt.dto.CreateTeacherRequest;
import com.portal.studymate.teachermgmt.dto.TeacherResponse;
import com.portal.studymate.teachermgmt.dto.UpdateTeacherRequest;

import java.util.List;

public interface TeacherService {

   TeacherResponse createTeacher(CreateTeacherRequest request);

   List<TeacherResponse> getAllTeachers();

   TeacherResponse updateTeacher(Long teacherId, UpdateTeacherRequest request);
}