package com.portal.studymate.common.exception;

public class ClassNotFoundException extends BusinessException {
   public ClassNotFoundException(Long id) {
      super("CLASS_NOT_FOUND", "Class not found: " + id);
   }
}
