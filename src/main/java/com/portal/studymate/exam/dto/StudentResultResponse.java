package com.portal.studymate.exam.dto;

import com.portal.studymate.exam.enums.ResultStatus;
import java.math.BigDecimal;

public record StudentResultResponse(
    Long studentId,
    String studentName,
    String admissionNumber,
    Integer totalMarks,
    Integer maxPossibleMarks,
    BigDecimal percentage,
    String grade,
    Integer rankInClass,
    ResultStatus resultStatus
) {}
