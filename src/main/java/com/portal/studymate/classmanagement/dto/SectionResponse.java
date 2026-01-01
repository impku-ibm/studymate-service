package com.portal.studymate.classmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {
   private Long id;
   private String name;
   private boolean active;
}
