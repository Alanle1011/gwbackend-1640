package com.example.backend1640.exception;

import org.springframework.http.HttpStatus;

public class SubmissionPeriodNotExistsException extends LocalizedException {
    public SubmissionPeriodNotExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
