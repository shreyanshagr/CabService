package com.cab.user.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(final MethodArgumentNotValidException ex) {
        final List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthErrors(final AuthenticationException ex) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, List<String>>> handleMediaTypeErrors(
            final HttpMediaTypeNotSupportedException ex) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<String>>> handleGeneralErrors(
            final Exception ex) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, List<String>>> handleExpiredErrors(
            final Exception ex) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, List<String>>> handleAuthenticationErrors(
            final Exception ex) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    private Map<String, List<String>> getErrorsMap(final List<String> errors) {
        final Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
