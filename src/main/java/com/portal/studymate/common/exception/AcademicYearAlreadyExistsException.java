package com.portal.studymate.common.exception;

public class AcademicYearAlreadyExistsException extends BusinessException {
   public AcademicYearAlreadyExistsException(String year) {
      super(
         "ACADEMIC_YEAR_ALREADY_EXISTS",
         "Academic year already exists: " + year
      );
   }
}
