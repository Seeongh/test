package com._3o3.demo.api.member.docs;

import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.common.ApiResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원가입 및 로그인", description = "회원가입 및 로그인 API")
public interface MemberControllerDocs {
    @Operation(summary = "회원가입", description = "Http Body로 전달된 정보로 회원가입 수행.")
    @ApiResponse(
            content = @Content(schema = @Schema(implementation = ApiResultResponse.class))
    )
    public ApiResultResponse<String> signup(@Valid @RequestBody MemberCreateDTO createDto);

    @Operation(summary = "로그인", description = "Http Body로 전달된 정보로 로그인 수행.")
    @ApiResponse(
            content = @Content(schema = @Schema(implementation = ApiResultResponse.class))
    )
    public ApiResultResponse<TockenDTO> login(@Valid @RequestBody MemberSignInDTO signDto);

    }
