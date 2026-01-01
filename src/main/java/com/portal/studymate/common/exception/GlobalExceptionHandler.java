package com.portal.studymate.common.exception;

import com.portal.studymate.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
       ResourceNotFoundException ex,
       HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ErrorResponse(
                                ex.getCode(),
                                ex.getMessage(),
                                Instant.now(),
                                request.getRequestURI()
                             ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
       ConflictException ex,
       HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponse(
                                ex.getCode(),
                                ex.getMessage(),
                                Instant.now(),
                                request.getRequestURI()
                             ));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
       BusinessException ex,
       HttpServletRequest request) {

        return ResponseEntity.badRequest()
                             .body(new ErrorResponse(
                                ex.getCode(),
                                ex.getMessage(),
                                Instant.now(),
                                request.getRequestURI()
                             ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
       Exception ex,
       HttpServletRequest request) {

        log.error("Unexpected error", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(
                                "INTERNAL_ERROR",
                                "Something went wrong",
                                Instant.now(),
                                request.getRequestURI()
                             ));
    }
}
