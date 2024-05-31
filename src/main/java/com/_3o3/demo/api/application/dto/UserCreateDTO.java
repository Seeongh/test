package com._3o3.demo.api.application.dto;

import com._3o3.demo.api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserCreateDTO {
    private String userId;
    private String password;
    private String name;
    private String regNo;

    public UserCreateDTO of(User user) {
        return UserCreateDTO
                .builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .name(user.getName())
                .regNo(user.getRegNo())
                .build();
    }
}
