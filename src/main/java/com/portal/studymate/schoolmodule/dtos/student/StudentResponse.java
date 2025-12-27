package com.portal.studymate.schoolmodule.dtos.student;

import com.portal.studymate.schoolmodule.utils.StudentStatus;

import java.time.LocalDate;

public record StudentResponse(
   Long id,
   String admissionNumber,
   String fullName,
   String parentName,
   String parentMobile,
   LocalDate admissionDate,
   StudentStatus status
) {}

