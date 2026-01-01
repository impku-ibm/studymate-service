package com.portal.studymate.school.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSchoolRequest {
   private String name;
   private String board;
   private String address;
   private String city;
   private String state;
   private String academicStartMonth;
   private String schoolCode;
}
