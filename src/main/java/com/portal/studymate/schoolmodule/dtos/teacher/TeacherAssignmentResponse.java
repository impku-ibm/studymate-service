package com.portal.studymate.schoolmodule.dtos.teacher;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherAssignmentResponse {
   private Long id;
   private Long teacherId;
   private String teacherName;

   private Long classId;
   private String section;

   private Long subjectId;
   private String subjectName;

   private Long academicYearId;
}
