package com._3o3.demo.api.user.presentation;

import com._3o3.demo.api.user.application.UserService;
import com._3o3.demo.api.user.application.dto.UserCreateDTO;
import com._3o3.demo.api.user.application.dto.UserSignInDTO;
import com._3o3.demo.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ApiResponse<Long> join(@Valid  @RequestBody UserCreateDTO createDto) {
        return userService.join(createDto);
    }

    @PostMapping(value = "/login")
    public ApiResponse<String> login(@Valid @RequestBody UserSignInDTO signDto) {

        return userService.login(signDto);
    }
}
