package com.portal.studymate.schoolmodule.service;

import com.portal.studymate.schoolmodule.dtos.AssignSubjectRequest;
import com.portal.studymate.schoolmodule.dtos.ClassSubjectResponse;

import java.util.List;

public interface ClassSubjectService {
   void assignSubjects(
      Long classId,
      Long sectionId,
      List<AssignSubjectRequest> subjects
   );

   List<ClassSubjectResponse> getSubjects(
      Long classId,
      Long sectionId
   );
   boolean isSubjectAssigned(
      Long classId,
      String section,
      Long subjectId
   );
}
