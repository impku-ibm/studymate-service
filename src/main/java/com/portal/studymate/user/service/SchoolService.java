package com.portal.studymate.user.service;

import com.portal.studymate.user.model.AcademicYear;
import com.portal.studymate.user.model.School;

import java.util.UUID;

public interface SchoolService {
   School createSchool(School school);
   AcademicYear createAcademicYear(UUID schoolId, String yearLabel);
}
