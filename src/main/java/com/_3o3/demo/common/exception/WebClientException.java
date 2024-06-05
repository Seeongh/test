package com._3o3.demo.common.exception;

public class WebClientException extends RuntimeException{
    public WebClientException(String message) {
        super(message);
    }

    public WebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
