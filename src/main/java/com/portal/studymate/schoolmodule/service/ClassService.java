package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.ClassResponse;
import com.portal.studymate.schoolmodule.dtos.CreateClassRequest;
import com.portal.studymate.schoolmodule.dtos.CreateSectionRequest;
import com.portal.studymate.schoolmodule.model.ClassEntity;

import java.util.List;

public interface ClassService {

   List<ClassResponse> getClasses();
   void addClass(CreateClassRequest request);
   ClassEntity get(Long classId);

}
