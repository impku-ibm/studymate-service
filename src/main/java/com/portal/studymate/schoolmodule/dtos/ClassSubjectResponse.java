package com.portal.studymate.schoolmodule.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassSubjectResponse {
   private Long id;
   private Long subjectId;
   private Integer weeklyPeriods;
   private boolean optional;
}
