package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.CreateAcademicYearRequest;
import com.portal.studymate.schoolmodule.model.AcademicYearEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AcademicYearService {
   List<AcademicYearEntity> getAll(String schoolId);

   AcademicYearEntity getActive(String schoolId);

   void addAcademicYear(String schoolId, CreateAcademicYearRequest request);

   void validateActiveYear(@NotNull Long academicYearId, String schoolId);
   AcademicYearEntity getNext(String schoolId);

}
