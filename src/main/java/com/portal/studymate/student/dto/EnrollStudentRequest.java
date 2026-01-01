package com.portal.studymate.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollStudentRequest {

   @NotNull
   private Long studentId;

   @NotNull
   private Long classId;

   @NotNull
   private Long sectionId;

   @NotNull
   private Integer rollNumber;
}

