package com.example.backend1640.exception;

import org.springframework.http.HttpStatus;

public class UploaderNotStudentException extends LocalizedException {
    public UploaderNotStudentException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
