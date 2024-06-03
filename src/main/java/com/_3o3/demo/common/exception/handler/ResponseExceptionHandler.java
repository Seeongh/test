package com._3o3.demo.common.exception.handler;


import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ApiResponse<Object> handleResponseException(CustomValidationException e) {

        e.getStackTrace();
        return ApiResponse.error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({ExpiredJwtException.class, io.jsonwebtoken.security.SecurityException.class, MalformedJwtException.class, UnsupportedJwtException.class})
    public ApiResponse<Object> expJwtTokenErr(ExpiredJwtException e) {
        e.getStackTrace();
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

}
