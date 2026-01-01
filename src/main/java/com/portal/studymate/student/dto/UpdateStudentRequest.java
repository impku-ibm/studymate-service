package com.portal.studymate.student.dto;

import com.portal.studymate.student.model.StudentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {

   @NotBlank
   private String fullName;

   private String address;

   private StudentStatus status;
}
