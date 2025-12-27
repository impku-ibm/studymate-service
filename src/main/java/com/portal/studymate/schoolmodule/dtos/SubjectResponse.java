package com.portal.studymate.schoolmodule.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectResponse {
   private Long id;
   private String name;
   private String code;
   private String category;
   private boolean active;
}
