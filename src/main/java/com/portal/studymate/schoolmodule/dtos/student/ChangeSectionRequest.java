package com.portal.studymate.schoolmodule.dtos.student;

import jakarta.validation.constraints.NotNull;

public record ChangeSectionRequest(
   @NotNull Long newSectionId
) {
}
