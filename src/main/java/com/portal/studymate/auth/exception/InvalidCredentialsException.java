package com.portal.studymate.auth.exception;

import com.portal.studymate.common.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {

   public InvalidCredentialsException(String invalidEmailOrPassword) {
      super(
         "INVALID_CREDENTIALS",
         "Invalid email or password"
      );
   }
}
