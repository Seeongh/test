package com._3o3.demo.api.user.application.dto;


import com._3o3.demo.api.user.domain.User;
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
    private String regNoBirth;
    private String regNoEnc;

    public UserDTO of(User user) {
        return UserDTO
                .builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .name(user.getName())
                .regNoBirth(user.getRegNoBirth())
                .regNoEnc(user.getRegNoEnc())
                .build();
    }
}
