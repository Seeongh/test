package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MemberSignInDTO {
    private String userId;
    private String password;
    private String role;

    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public Member toEntity() {
        return Member.
                builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
