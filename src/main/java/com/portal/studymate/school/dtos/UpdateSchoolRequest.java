package com.portal.studymate.school.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSchoolRequest {
   private String name;
   private String board;
   private String address;
   private String city;
   private String state;
   private String academicStartMonth;
}

