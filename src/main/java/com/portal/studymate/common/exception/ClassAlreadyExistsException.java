package com.portal.studymate.common.exception;

public class ClassAlreadyExistsException extends BusinessException {
   public ClassAlreadyExistsException(String className) {
      super("CLASS_ALREADY_EXISTS", "Class already exists: " + className);
   }
}

