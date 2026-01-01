package com.portal.studymate.school.service;


import com.portal.studymate.school.dtos.CreateSchoolRequest;
import com.portal.studymate.school.dtos.SchoolResponse;
import com.portal.studymate.school.dtos.UpdateSchoolRequest;

public interface SchoolService {

   SchoolResponse getCurrentSchool();

   SchoolResponse createSchool(CreateSchoolRequest request);

   SchoolResponse updateSchool(UpdateSchoolRequest request);
}


