package com._3o3.demo.api.application.dto;

import com._3o3.demo.api.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserCreateDTO implements UserDetails {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String regNo;

    public User toEntity(String passwordEnc, String regNoEnc) {
        return User
                .builder()
                .userId(userId)
                .password(passwordEnc)
                .name(name)
                .regNo(regNoEnc)
                .build();
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return toEntity(passwordEncoder.encode(password), passwordEncoder.encode(regNo));
    }

    @Override //권한
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override //
    public String getUsername() {
        return null;
    }
}
