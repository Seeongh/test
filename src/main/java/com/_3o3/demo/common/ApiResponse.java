package com._3o3.demo.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

    private int httpStatus;
    private String responseMessage;

    private T data;

    public ApiResponse(HttpStatus httpStatus, String responseMessage, T data) {
        this.httpStatus = httpStatus.value();
        this.responseMessage = responseMessage;
        this.data = data;
    }


    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
       // String message = getDefaultMessageForStatusCode(status);
        String responseMessage = "";
        return new ApiResponse<>(httpStatus, responseMessage, data);
    }

}
