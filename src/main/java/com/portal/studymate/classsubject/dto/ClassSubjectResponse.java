package com.portal.studymate.classsubject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassSubjectResponse {

   private Long id;
   private Long subjectId;
   private String subjectName;
   private String subjectCode;
}
