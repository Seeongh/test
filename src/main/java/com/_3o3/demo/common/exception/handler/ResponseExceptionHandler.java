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

import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public ApiResponse<Object> handleResponseException(CustomValidationException e) {

        e.getStackTrace();
        return ApiResponse.error(HttpStatus.CONFLICT, e.getMessage());
    }


}
