package com._3o3.demo.api.application.dto;

import com._3o3.demo.api.domain.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class UserSignInDTO {
    private String userId;
    private String password;
    private String role;

    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public User toEntity() {
        return User.
                builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
