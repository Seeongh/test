package com._3o3.demo.api.member.application.dto;

import com._3o3.demo.api.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MemberSignInDTO {
    @NotBlank(message = "사용자 아이디를 입력하세요.")
    private String userId;
    @NotBlank(message = "사용자 비밀번호를 입력하세요.")
    private String password;
    private String role;

    public List<String> getRoleList() {
        if(!this.role.isEmpty()) {
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
