package com.portal.studymate.academicyear.service;
import com.portal.studymate.academicyear.dto.AcademicYearResponse;
import com.portal.studymate.academicyear.dto.CreateAcademicYearRequest;

import java.util.List;

public interface AcademicYearService {

   AcademicYearResponse createAcademicYear(CreateAcademicYearRequest request);

   AcademicYearResponse getActiveAcademicYear();

   List<AcademicYearResponse> getAllAcademicYears();
}
