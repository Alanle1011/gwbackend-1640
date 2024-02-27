package com.example.backend1640.handler;

import com.example.backend1640.exception.LocalizedException;
import jakarta.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String UNEXPECTED_ERROR_MSG = "The system has encountered an error. If this error message persists, contact Customer Support.";

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(BindException ex) {
        logException(ex);
        List<String> errors = ex.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(createBadRequestError(errors, StringUtils.EMPTY));
    }

    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<Map<String, Object>> handleLocalizedException(LocalizedException ex) {
        logException(ex);
        String message = messageSource.getMessage(ex.getMessage(), ex.getParameters(), ex.getMessage(), Locale.getDefault());
        return ResponseEntity.status(ex.getHttpStatus()).body(createError(ex.getHttpStatus(), message, Collections.emptyList(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerError(Exception ex) throws Exception {
        logException(ex);
        if (ex instanceof ServletException) {
            throw ex;
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createError(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_MSG, Collections.emptyList(), ex.getMessage()));
    }

    private Map<String, Object> createError(HttpStatus status, String message, List<String> errors, String exceptionMessage) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", DateFormatUtils.formatUTC(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        if (!CollectionUtils.isEmpty(errors)) {
            error.put("errors", errors);
        }
        if (StringUtils.isNotBlank(exceptionMessage)) {
            error.put("detailError", exceptionMessage);
        }
        return error;
    }

    private Map<String, Object> createUnauthorizedError() {
        return createError(HttpStatus.UNAUTHORIZED, "Unauthorized", Collections.emptyList(), null);
    }

    private Map<String, Object> createNotFoundError() {
        return createError(HttpStatus.NOT_FOUND, "Not Found", Collections.emptyList(), null);
    }

    private Map<String, Object> createBadRequestError(List<String> errors, String errorMessage) {
        return createError(HttpStatus.BAD_REQUEST, "Invalid input", errors, errorMessage);
    }

    private void logException(Exception ex) {
        LOGGER.debug(ex.getMessage(), ex);
    }
}
