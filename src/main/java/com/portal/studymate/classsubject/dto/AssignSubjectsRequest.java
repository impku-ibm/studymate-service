package com.portal.studymate.classsubject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubjectsRequest {

   @NotNull
   private Long classId;

   @NotEmpty
   private List<Long> subjectIds;
}

