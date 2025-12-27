package com.portal.studymate.schoolmodule.dtos.teacher;

import com.portal.studymate.schoolmodule.model.TeacherStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTeacherRequest {

   @NotBlank
   private String fullName;

   @NotBlank
   private String mobileNumber;

   @NotBlank
   private String qualification;

   private String notes;

   private TeacherStatus status;
}

