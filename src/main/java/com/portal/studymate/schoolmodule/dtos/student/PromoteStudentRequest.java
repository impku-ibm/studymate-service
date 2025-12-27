package com.portal.studymate.schoolmodule.dtos.student;

import jakarta.validation.constraints.NotNull;

public record PromoteStudentRequest(@NotNull Long nextClassId,
                                    @NotNull Long nextSectionId) {
}
