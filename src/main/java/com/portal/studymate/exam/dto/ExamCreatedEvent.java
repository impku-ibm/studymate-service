package com.portal.studymate.exam.dto;

public record ExamCreatedEvent(
    Long examId,
    Long schoolId,
    Long academicYearId
) {}