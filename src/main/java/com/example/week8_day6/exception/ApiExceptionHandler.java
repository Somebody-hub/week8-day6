package com.example.week8_day6.exception;

import com.example.week8_day6.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.StringJoiner;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        StringJoiner joiner = new StringJoiner("; ");
        for (FieldError error : fieldErrors) {
            String fieldName = error.getField();
            String defaultMessage = error.getDefaultMessage();
            joiner.add(fieldName + ": " + defaultMessage);
        }

        ApiError apiError = new ApiError("INVALID_INPUT", joiner.toString(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(TaskNotFoundException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError("TASK_NOT_FOUND", e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidJson(HttpMessageNotReadableException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("MALFORMED_JSON", "Invalid JSON format or unknown enum value", request.getRequestURI()));
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleTagAlreadyExists(TagAlreadyExistsException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError("TAG_ALREADY_EXISTS", e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String msg = String.format("Parameter '%s' has invalid value '%s'", e.getName(), e.getValue());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_PARAMETER", msg, request.getRequestURI()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String msg = String.format("Method '%s' is not supported for this endpoint", e.getMethod());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiError("METHOD_NOT_ALLOWED", msg, request.getRequestURI()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String msg = String.format("Content type '%s' is not supported", e.getContentType());
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiError("UNSUPPORTED_MEDIA_TYPE", msg, request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError("CONSTRAINT_VIOLATION",
                        "The record violates a database constraint",
                        request.getRequestURI()));

    }

    @ExceptionHandler(StatusTransitionException.class)
    public ResponseEntity<ApiError> handleStatusTransaction(StatusTransitionException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("INVALID_STATUS_TRANSITION", e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_SERVER_ERROR", e.getMessage(), request.getRequestURI()));
    }
}
