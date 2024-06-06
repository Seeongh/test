package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.util.AES256Util;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberCreateDTO {
    @NotBlank(message = "사용자 아이디를 입력하세요.")
    private String userId;

    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String name;

    @NotBlank(message = "주민등록번호를 입력하세요.")
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
