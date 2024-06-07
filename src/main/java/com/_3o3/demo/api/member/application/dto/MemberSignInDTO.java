package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "[로그인 요청 정보]")
public class MemberSignInDTO {
    @Schema(description = "로그인 할 사용자 아이디(필수)", example = "kw68")
    @NotBlank(message = "사용자 아이디를 입력하세요.")
    private String userId;

    @Schema(description = "로그인 할 사용자 비밀번호(필수)", example = "123456")
    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;

    public Member toEntity() {
        return Member.
                builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
