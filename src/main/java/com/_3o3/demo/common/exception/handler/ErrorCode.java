package com._3o3.demo.common.exception.handler;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR(1001, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(1002, "변조된 토큰입니다."),
    EXPIRED_TOKEN(1003, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1004, "변조된 토큰입니다."),
    ACCESS_DENIED(1005, "권한이 없습니다.");


    private int code;
    private String message;

    ErrorCode(int status, String message) {
        this.code = status;
        this.message = message;
    }

}
