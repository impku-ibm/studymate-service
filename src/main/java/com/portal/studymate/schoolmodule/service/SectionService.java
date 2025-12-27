package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.CreateSectionRequest;
import com.portal.studymate.schoolmodule.dtos.SectionResponse;
import com.portal.studymate.schoolmodule.model.SectionEntity;

import java.util.List;

public interface SectionService {
   List<SectionResponse> getSections(Long classId);

   SectionEntity get(Long sectionId, Long classId);

   void addSection(Long classId, CreateSectionRequest request);
}
