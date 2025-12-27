package com.portal.studymate.schoolmodule.dtos.student;

import jakarta.validation.constraints.NotNull;

public record EnrollStudentRequest(
   @NotNull Long studentId,
   @NotNull Long classId,
   @NotNull Long sectionId
) {}
