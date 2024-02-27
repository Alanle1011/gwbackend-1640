package com.example.backend1640.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LocalizedException extends RuntimeException {
    private Object[] parameters;

    protected LocalizedException(String message) {
        super(message);
    }

    protected LocalizedException(String message, Object[] parameters) {
        super(message);
        this.parameters = parameters;
    }

    public abstract HttpStatus getHttpStatus();
}
