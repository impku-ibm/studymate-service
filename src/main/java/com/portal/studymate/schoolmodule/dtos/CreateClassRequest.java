package com.portal.studymate.schoolmodule.dtos;

import jakarta.validation.constraints.NotBlank;


public record CreateClassRequest(
   @NotBlank String className
) {}
