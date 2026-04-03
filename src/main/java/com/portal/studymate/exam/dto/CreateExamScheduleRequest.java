package com.portal.studymate.exam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record CreateExamScheduleRequest(
    @NotNull Long examId,
    @NotNull Long classId,
    @NotBlank String section,
    @NotNull Long subjectId,
    @NotNull LocalDate examDate,
    @NotNull @Positive Integer maxMarks,
    @NotNull @Min(0) Integer passMarks,
    @NotNull @Positive Integer durationMinutes
) {}