package com.portal.studymate.common.exception;

public class SchoolNotFoundException extends RuntimeException {
    public SchoolNotFoundException(String message) {
        super(message);
    }
}