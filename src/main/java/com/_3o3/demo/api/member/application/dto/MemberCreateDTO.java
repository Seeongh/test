package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.util.AES256Util;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
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
