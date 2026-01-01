package com.portal.studymate.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {
   public UserAlreadyExistsException(String message) {
      super(message);
   }
}