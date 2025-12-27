package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.SubjectCreateRequest;
import com.portal.studymate.schoolmodule.dtos.SubjectResponse;
import com.portal.studymate.schoolmodule.dtos.SubjectUpdateRequest;

import java.util.List;

public interface SubjectMasterService {
   SubjectResponse create(SubjectCreateRequest request);

   List<SubjectResponse> getAllActive();

   SubjectResponse update(Long subjectId, SubjectUpdateRequest request);
   String getNameById(Long subjectId);
}
