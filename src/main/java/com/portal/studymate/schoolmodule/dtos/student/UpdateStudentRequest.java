package com.portal.studymate.schoolmodule.dtos.student;

import com.portal.studymate.schoolmodule.utils.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateStudentRequest(
   @NotBlank String fullName,
   @NotNull LocalDate dateOfBirth,
   @NotNull Gender gender,
   @NotBlank String parentName,
   @NotBlank String parentMobile,
   @NotBlank String address
) {}
