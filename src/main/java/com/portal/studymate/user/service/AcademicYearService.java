package com.portal.studymate.user.service;

import com.portal.studymate.user.dtos.CreateAcademicYearRequest;
import com.portal.studymate.user.model.AcademicYearEntity;

import java.util.List;

public interface AcademicYearService {
   List<AcademicYearEntity> getAll(String schoolId);

   AcademicYearEntity getActive(String schoolId);

   void addAcademicYear(String schoolId, CreateAcademicYearRequest request);
}
