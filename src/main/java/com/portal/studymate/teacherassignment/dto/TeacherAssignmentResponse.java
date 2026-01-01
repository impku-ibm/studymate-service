package com.portal.studymate.teacherassignment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherAssignmentResponse {

   private Long id;

   private Long teacherId;
   private String teacherName;

   private Long subjectId;
   private String subjectName;

   private Long sectionId;
   private String sectionName;

   private boolean active;
}

