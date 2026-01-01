package com.portal.studymate.teachermgmt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherResponse {

   private Long id;
   private String fullName;
   private String email;
   private String mobileNumber;
   private String teacherCode;
   private String qualification;
   private String notes;
   private boolean active;
}
