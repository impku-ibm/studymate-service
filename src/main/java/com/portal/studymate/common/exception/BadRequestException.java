package com.portal.studymate.common.exception;

public class BadRequestException extends BusinessException {

   public BadRequestException(String code, String message) {
      super(code, message);
   }
}

