package com.portal.studymate.classmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassResponse {
   private Long id;
   private String name;
   private String section;
   private String code;
   private boolean active;
}
