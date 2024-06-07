package com._3o3.demo.api.member.presentation;

import com._3o3.demo.api.member.application.MemberService;
import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.api.member.docs.MemberControllerDocs;
import com._3o3.demo.common.ApiResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 가입 및 로그인 처리
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResultResponse<String> signup(@Valid  @RequestBody MemberCreateDTO createDto) {
        return memberService.signup(createDto);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResultResponse<TockenDTO> login(@Valid @RequestBody MemberSignInDTO signDto) {
        return memberService.login(signDto);
    }
}
