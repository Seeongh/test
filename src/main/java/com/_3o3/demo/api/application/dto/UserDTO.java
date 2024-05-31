package com._3o3.demo.api.application.dto;


import com._3o3.demo.api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDTO {

    private String userId;
    private String password;
    private String name;
    private String regNo;

    public UserDTO of(User user) {
        return UserDTO
                .builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .name(user.getName())
                .regNo(user.getRegNo())
                .build();
    }
}
