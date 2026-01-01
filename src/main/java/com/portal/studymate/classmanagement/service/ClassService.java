package com.portal.studymate.classmanagement.service;

import com.portal.studymate.classmanagement.dto.ClassResponse;
import com.portal.studymate.classmanagement.dto.CreateClassRequest;
import com.portal.studymate.classmanagement.dto.UpdateClassRequest;

import java.util.List;

public interface ClassService {

   ClassResponse createClass(CreateClassRequest request);

   List<ClassResponse> getAllClasses();

   ClassResponse updateClass(Long classId, UpdateClassRequest request);
}