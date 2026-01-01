package com.portal.studymate.student.dto;

import com.portal.studymate.student.model.StudentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentResponse {

   private Long id;
   private String admissionNumber;
   private String fullName;
   private String parentName;
   private String parentMobile;
   private LocalDate admissionDate;
   private StudentStatus status;
}

