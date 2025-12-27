package com.portal.studymate.schoolmodule.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record CreateSectionRequest(
   @NotBlank String name
) {}
