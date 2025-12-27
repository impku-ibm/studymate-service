package com.portal.studymate.schoolmodule.dtos.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignTeacherRequest {

   @NotNull
   private Long teacherId;

   @NotNull
   private Long classId;

   @NotBlank
   private String section;

   @NotNull
   private Long subjectId;

   @NotNull
   private Long academicYearId;
}

