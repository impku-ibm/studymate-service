package com.portal.studymate.exam.dto;

import com.portal.studymate.exam.enums.ExamType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;

import java.time.LocalDate;

public record CreateExamRequest(
    @NotNull Long academicYearId,
    @NotNull ExamType examType,
    @NotBlank String name,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate
) {}