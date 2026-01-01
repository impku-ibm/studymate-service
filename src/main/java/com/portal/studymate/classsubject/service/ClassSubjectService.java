package com.portal.studymate.classsubject.service;

import com.portal.studymate.classsubject.dto.AssignSubjectsRequest;
import com.portal.studymate.classsubject.dto.ClassSubjectResponse;

import java.util.List;

public interface ClassSubjectService {

   List<ClassSubjectResponse> assignSubjects(AssignSubjectsRequest request);

   List<ClassSubjectResponse> getSubjectsForClass(Long classId);
}

