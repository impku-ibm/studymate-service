package com.portal.studymate.teacherassignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherAssignmentRequest {

   @NotNull
   private Long teacherId;

   @NotNull
   private Long sectionId;

   @NotNull
   private Long subjectId;
}

