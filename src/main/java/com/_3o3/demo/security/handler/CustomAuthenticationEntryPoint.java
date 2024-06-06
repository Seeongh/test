package com._3o3.demo.security.handler;

import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.handler.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info(" entry point exception occur {}", authException.getMessage());
        String exceptionMessage = (String)request.getAttribute("exception");

        ApiResponse<String> exceptionResponse = null;
        if(exceptionMessage == null) {
            exceptionResponse =ApiResponse.of(HttpStatus.UNAUTHORIZED, authException.getMessage());
        }
        else{
            exceptionResponse =  ApiResponse.of(HttpStatus.UNAUTHORIZED, exceptionMessage);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, exceptionResponse);
            os.flush();
        }

    }

}