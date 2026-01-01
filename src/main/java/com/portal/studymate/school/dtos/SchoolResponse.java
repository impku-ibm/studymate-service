package com.portal.studymate.school.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class SchoolResponse {

   private Long id;
   private String name;
   private String board;
   private String city;
   private String state;
   private String academicStartMonth;
   private String schoolCode;
}

