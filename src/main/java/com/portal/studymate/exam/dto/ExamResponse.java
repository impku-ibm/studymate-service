package com.portal.studymate.exam.dto;

import com.portal.studymate.exam.enums.ExamStatus;
import com.portal.studymate.exam.enums.ExamType;

import java.time.LocalDate;

public record ExamResponse(
    Long id,
    Long academicYearId,
    String academicYearName,
    ExamType examType,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    Boolean publishResult,
    ExamStatus status
) {}