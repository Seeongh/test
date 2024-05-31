package com._3o3.demo.api.presentation;

import com._3o3.demo.api.application.dto.UserCreateDTO;
import com._3o3.demo.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 가입 및 로그인 처리
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class UserController {

    @PostMapping()
    public ApiResponse<String> join(@ModelAttribute UserCreateDTO createDTO) {
        return ApiResponse.of(HttpStatus.OK, "");
    }

}
