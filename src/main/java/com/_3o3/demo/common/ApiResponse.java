package com._3o3.demo.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@NoArgsConstructor
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
        String responseMessage = getCustomMessageForStatusCode(httpStatus);
        log.info("Response status, data = {}, {}", httpStatus, data);
        return new ApiResponse<>(httpStatus, responseMessage, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        log.info("Response failed  httpStatus, message = {}, {}", httpStatus, message) ;
        return new ApiResponse<>(httpStatus, message, null);
    }

    public static String getCustomMessageForStatusCode(HttpStatus httpStatus) {
        switch (httpStatus) {
            case OK :
                return "성공 했습니다." ;
            case UNAUTHORIZED:
                return "인증 오류가 발생했습니다.";
            default :
                return "";
        }
    }

}
