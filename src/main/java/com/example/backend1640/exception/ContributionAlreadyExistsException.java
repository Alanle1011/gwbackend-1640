package com.sss.pandax.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends LocalizedException {
    public UserAlreadyExistsException(String message, Object[] objects) {
        super(message, objects);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
