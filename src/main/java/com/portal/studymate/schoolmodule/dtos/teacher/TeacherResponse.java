package com.portal.studymate.schoolmodule.dtos.teacher;

import com.portal.studymate.schoolmodule.model.TeacherStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherResponse {
   private Long id;
   private String teacherCode;
   private String fullName;
   private String email;
   private String mobileNumber;
   private String qualification;
   private TeacherStatus status;
}
