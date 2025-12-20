package com.portal.studymate.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateSchoolRequest(
   @NotBlank String name,
   @NotBlank String board,
   String address
) {}
