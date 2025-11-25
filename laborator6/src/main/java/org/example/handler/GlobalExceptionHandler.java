package org.example.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.example.error.ApiError;
import org.example.error.FieldErrorDetail;
import org.example.exception.NotFoundException;
import org.example.exception.ResourceConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400: @Valid on @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorDetail(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()))
                .collect(toList());
        return build(HttpStatus.BAD_REQUEST, ex, req, "Validation failed", details);
    }

    // 400: @Validated on @PathVariable/@RequestParam constraints
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<FieldErrorDetail> details = ex.getConstraintViolations().stream()
                .map(cv -> new FieldErrorDetail(
                        cv.getPropertyPath().toString(),
                        cv.getInvalidValue(),
                        cv.getMessage()))
                .collect(toList());
        return build(HttpStatus.BAD_REQUEST, ex, req, "Validation failed", details);
    }

    // 400: malformed JSON / wrong types in body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex, req, rootMessage(ex));
    }

    // 400: path variable type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = String.format("Parameter '%s' value '%s' could not be converted to %s",
                ex.getName(), ex.getValue(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "required type");
        return build(HttpStatus.BAD_REQUEST, ex, req, msg);
    }

    // 404: no handler found (enabled via application properties below)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex, req, ex.getMessage());
    }

    // 405: wrong HTTP method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex, req, ex.getMessage());
    }

    // 415: unsupported media type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex, req, ex.getMessage());
    }

    // Custom domain/business exceptions → decide status per your use-case
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ResourceConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex, req, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex, req, "User not found");
    }

    // 500: any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, req, "Unexpected error");
    }

    private ResponseEntity<ApiError> build(HttpStatus status, Exception ex, HttpServletRequest req, String message) {
        return build(status, ex, req, message, null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, Exception ex, HttpServletRequest req, String message, List<FieldErrorDetail> details) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getClass().getName(),
                message,
                req.getRequestURI(),
                details
        );
        return ResponseEntity.status(status).body(body);
    }

    private String rootMessage(Throwable t) {
        String m = t.getMessage();
        return StringUtils.hasText(m) ? m : t.getClass().getSimpleName();
    }
}