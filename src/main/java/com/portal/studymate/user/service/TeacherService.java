package com.portal.studymate.user.service;

import com.portal.studymate.user.dtos.AssignTeacherRequest;
import com.portal.studymate.user.dtos.CreateTeacherRequest;
import com.portal.studymate.user.model.Teacher;
import com.portal.studymate.user.model.TeacherAssignment;

import java.util.UUID;

public interface TeacherService {
   Teacher addTeacher(UUID schoolId, CreateTeacherRequest req);
   TeacherAssignment assign(AssignTeacherRequest req);
}
