package com._3o3.demo.api.application.dto;

import com._3o3.demo.api.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateDTO {
    private String userId;
    private String password;
    private String name;
    private String regNo;

    public User toEntity() {
        return User
                .builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();
    }
}
