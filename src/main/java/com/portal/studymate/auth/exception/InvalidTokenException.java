package com.portal.studymate.auth.exception;

public class InvalidTokenException extends RuntimeException {
   public InvalidTokenException(String message) {
      super(message);
   }
}
