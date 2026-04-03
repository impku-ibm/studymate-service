package com.portal.studymate.exam.dto;

import java.time.LocalDate;

public record ExamScheduleResponse(
    Long id,
    Long examId,
    Long classId,
    String className,
    String section,
    Long subjectId,
    String subjectName,
    LocalDate examDate,
    Integer maxMarks,
    Integer passMarks,
    Integer durationMinutes
) {}
