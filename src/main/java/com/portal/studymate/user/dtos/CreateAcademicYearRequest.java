package com.portal.studymate.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateAcademicYearRequest(@NotBlank String yearLabel) {
}
