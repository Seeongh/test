package com._3o3.demo.api.member.presentation;

import com._3o3.demo.api.member.application.MemberService;
import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
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
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ApiResponse<Long> join(@Valid  @RequestBody MemberCreateDTO createDto) {
        return memberService.join(createDto);
    }

    @PostMapping(value = "/login")
    public ApiResponse<TockenDTO> login(@Valid @RequestBody MemberSignInDTO signDto) {
        return memberService.login(signDto);
    }
}
