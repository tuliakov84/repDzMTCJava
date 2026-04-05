package com.mipt.sem2.hw1.todolist.exception;

import com.mipt.sem2.hw1.todolist.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                  HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      details.put(fieldName, errorMessage);
    });
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message("Validation failed")
        .path(request.getRequestURI())
        .details(details)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                 HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      details.put(violation.getPropertyPath().toString(), violation.getMessage());
    });
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message("Constraint violation")
        .path(request.getRequestURI())
        .details(details)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                             HttpServletRequest request) {
    log.warn("Illegal argument: {}", ex.getMessage());
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(AttachmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAttachmentNotFound(AttachmentNotFoundException ex,
                                                                HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex,
                                                           HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message("Missing parameter: " + ex.getParameterName())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                    HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message("Malformed JSON request")
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex,
                                                            HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex,
                                                             HttpServletRequest request) {
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
    log.error("Unhandled exception", ex);
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Internal Server Error")
        .message("An unexpected error occurred")
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}