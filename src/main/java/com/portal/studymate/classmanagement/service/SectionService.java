package com.portal.studymate.classmanagement.service;

import com.portal.studymate.classmanagement.dto.CreateSectionRequest;
import com.portal.studymate.classmanagement.dto.SectionResponse;
import com.portal.studymate.classmanagement.dto.UpdateSectionRequest;

import java.util.List;

public interface SectionService {

   SectionResponse createSection(Long classId, CreateSectionRequest request);

   List<SectionResponse> getSectionsByClass(Long classId);

   SectionResponse updateSection(Long sectionId, UpdateSectionRequest request);
}
