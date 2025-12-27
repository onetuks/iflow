package com.onetuks.ihub.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFound(
      EntityNotFoundException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.NOT_FOUND,
        "ENTITY_NOT_FOUND",
        ex.getMessage(),
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .toList();
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "VALIDATION_FAILED",
        "Validation failed",
        errors,
        request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    List<String> errors = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .toList();
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "CONSTRAINT_VIOLATION",
        "Constraint violation",
        errors,
        request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiError> handleAccessDenied(
      AccessDeniedException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.FORBIDDEN,
        "ACCESS_DENIED",
        ex.getMessage(),
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ApiError> handleSpringAccessDenied(
      org.springframework.security.access.AccessDeniedException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.FORBIDDEN,
        "ACCESS_DENIED",
        ex.getMessage(),
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
  public ResponseEntity<ApiError> handleAuthenticationFailures(Exception ex, WebRequest request) {
    return buildResponse(
        HttpStatus.UNAUTHORIZED,
        "UNAUTHORIZED",
        "Invalid username or password",
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler({LockedException.class, DisabledException.class})
  public ResponseEntity<ApiError> handleAccountState(RuntimeException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.FORBIDDEN,
        "ACCESS_DENIED",
        ex.getMessage(),
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, WebRequest request) {
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "MALFORMED_JSON",
        "Malformed JSON request",
        Collections.emptyList(),
        request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex, WebRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_ERROR",
        "Unexpected error occurred",
        List.of(ex.getMessage()),
        request);
  }

  private ResponseEntity<ApiError> buildResponse(
      HttpStatus status,
      String code,
      String message,
      List<String> errors,
      WebRequest request) {
    String path = resolvePath(request);
    ApiError apiError = ApiError.of(status.value(), code, message, errors, path);
    return ResponseEntity.status(status).body(apiError);
  }

  private String resolvePath(WebRequest request) {
    if (request instanceof ServletWebRequest servletWebRequest) {
      HttpServletRequest httpRequest = servletWebRequest.getRequest();
      return httpRequest.getRequestURI();
    }
    return "";
  }
}
