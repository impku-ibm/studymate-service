package com.portal.studymate.common.exception;

public class AcademicYearNotFoundException extends BusinessException {
   public AcademicYearNotFoundException() {
      super("ACADEMIC_YEAR_NOT_FOUND", "No active academic year found");
   }
}
