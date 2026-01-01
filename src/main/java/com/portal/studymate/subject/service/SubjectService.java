package com.portal.studymate.subject.service;

import com.portal.studymate.subject.dto.CreateSubjectRequest;
import com.portal.studymate.subject.dto.SubjectResponse;
import com.portal.studymate.subject.dto.UpdateSubjectRequest;

import java.util.List;

public interface SubjectService {

   SubjectResponse createSubject(CreateSubjectRequest request);

   List<SubjectResponse> getAllSubjects();

   SubjectResponse updateSubject(Long subjectId, UpdateSubjectRequest request);
}
