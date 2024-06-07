package com._3o3.demo.common.exception.handler;


import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.common.exception.WebClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseExceptionHandler {

    /**
     * 로직 수행 중 발생한 에러 처리
     * @param e
     * @return  HttpStatus : 설정한 에러 <br>
     *          message : 해당 에러 던질때 설정한 message <br>
     */
    @ExceptionHandler(CustomValidationException.class)
    public ApiResultResponse<Object> handleResponseException(CustomValidationException e) {
        e.getStackTrace();
        if(e.getHttpStatus()!= null) {
            return ApiResultResponse.error(e.getHttpStatus(), e.getMessage());
        }
        return ApiResultResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 웹통신중 발생한 에러 처리
     * @param e
     * @return  HttpStatus : 503 <br>
     *          message : 해당 에러 던질때 설정한 message <br>
     */
    @ExceptionHandler(WebClientException.class)
    public ApiResultResponse<Object> handleWebClientException(WebClientException e) {

        e.getStackTrace();
        return ApiResultResponse.error(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }
    /**
     * "@Valid" 사용시 유효성검사 에러 처리 <br>
     * @param
     * @return
     *         HttpStatus : 400 <br>
     *         message : DTO 에서 변수에 설정한 message <br>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public static ApiResultResponse<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        return ApiResultResponse.error(HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }



}
