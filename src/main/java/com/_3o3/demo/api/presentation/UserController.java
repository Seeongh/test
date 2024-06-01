package com._3o3.demo.api.presentation;

import com._3o3.demo.api.application.UserService;
import com._3o3.demo.api.application.dto.UserCreateDTO;
import com._3o3.demo.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 가입 및 로그인 처리
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ApiResponse<Long> join(@RequestBody UserCreateDTO createDto) {
        return userService.join(createDto);
    }

}
