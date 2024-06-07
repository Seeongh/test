package com._3o3.demo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@NoArgsConstructor
@Schema(description = "[응답 정보]")
public class ApiResultResponse<T> {

    @Schema(description = "요청 수행 응답 Http 코드", example = "성공 : 200, 실패 : 1xx, 4xx, 5xx")
    private HttpStatus httpStatus;
    @Schema(description = "Client에게 전달할 메세지", example = "성공했습니다.")
    private String responseMessage;
    @Schema(description = "반환되는 데이터", example = "<사용자 이름>")
    private T data;

    public ApiResultResponse(HttpStatus httpStatus, String responseMessage, T data) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public static <T> ApiResultResponse<T> of(HttpStatus httpStatus, T data) {
        String responseMessage = getCustomMessageForStatusCode(httpStatus);
        log.info("Response status, data = {}, {}", httpStatus, data);
        return new ApiResultResponse<>(httpStatus, responseMessage, data);
    }

    public static <T> ApiResultResponse<T> error(HttpStatus httpStatus, String message) {
        log.info("Response failed  httpStatus, message = {}, {}", httpStatus, message) ;
        return new ApiResultResponse<>(httpStatus, message, null);
    }

    public static String getCustomMessageForStatusCode(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case OK -> "성공 했습니다.";
            case UNAUTHORIZED -> "인증 오류가 발생했습니다.";
            default -> "";
        };
    }

}
