package com._3o3.demo.api.user.application.dto;

import com._3o3.demo.api.user.domain.User;
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
public class UserCreateDTO {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String regNo;

    public User toEntity(String passwordEnc) {
        String[] regData = regNo.split("-");

        return User
                .builder()
                .userId(userId)
                .password(passwordEnc)
                .name(name)
                .regNoBirth(regData[0])
                .regNoEnc(AES256Util.encrypt(regData[1]))
                .build();
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return toEntity(passwordEncoder.encode(password));
    }

}
