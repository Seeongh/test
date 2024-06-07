package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.util.AES256Util;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Schema(description = "[회원가입 요청 정보]")
public class MemberCreateDTO {
    @Schema(description = "사용자 아이디(필수)", example = "kw68")
    @NotBlank(message = "사용자 아이디를 입력하세요.")
    private String userId;

    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    @Schema(description = "사용자 비밀번호(필수)", example = "123456")
    private String password;

    @NotBlank(message = "사용자 이름을 입력하세요.")
    @Schema(description = "사용자 이름(필수)", example = "관우")
    private String name;

    @NotBlank(message = "주민등록번호를 입력하세요.")
    @Schema(description = "사용자 주민번호(필수)", example = "681108-1582816")
    @Pattern(regexp = "^\\d{6}-[1-4]\\d{6}$", message = "주민등록번호 형식이 올바르지 않습니다.")
    private String regNo;

    public Member toEntity(String passwordEnc) {
        String[] regData = regNo.split("-");

        return Member
                .builder()
                .userId(userId)
                .password(passwordEnc)
                .name(name)
                .regNoBirth(regData[0])
                .regNoEnc(AES256Util.encrypt(regData[1]))
                .build();
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return toEntity(passwordEncoder.encode(password));
    }

}
