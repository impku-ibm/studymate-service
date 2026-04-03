package com.portal.studymate.student.dto;

import com.portal.studymate.student.model.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {
   private String fullName;
   private LocalDate dateOfBirth;
   private String parentName;
   private String parentMobile;
   private String address;
   private StudentStatus status;
}
