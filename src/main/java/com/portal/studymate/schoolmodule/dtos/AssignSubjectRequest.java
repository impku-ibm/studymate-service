package com.portal.studymate.schoolmodule.dtos;

import lombok.Data;

@Data
public class AssignSubjectRequest {
   private Long subjectId;

   private Integer weeklyPeriods;

   private boolean optional;
}
