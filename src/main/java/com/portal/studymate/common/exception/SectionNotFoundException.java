package com.portal.studymate.common.exception;

public class SectionNotFoundException extends BusinessException {
   public SectionNotFoundException(Long id) {
      super("CLASS_NOT_FOUND", "Class not found: " + id);
   }
}
