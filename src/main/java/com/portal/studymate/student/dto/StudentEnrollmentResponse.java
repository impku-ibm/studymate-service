package com.portal.studymate.student.dto;

import com.portal.studymate.student.model.EnrollmentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentEnrollmentResponse {

   private Integer rollNumber;
   private String studentName;
   private String admissionNumber;
   private String className;
   private String sectionName;
   private EnrollmentStatus status;
}
