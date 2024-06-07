package com._3o3.demo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomValidationException extends RuntimeException{
    HttpStatus httpStatus;
    public CustomValidationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
