package com._3o3.demo.api.member.application.dto;


import com._3o3.demo.api.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDTO {

    private String userId;
    private String password;
    private String name;
    private String regNoBirth;
    private String regNoEnc;

    public MemberDTO of(Member member) {
        return MemberDTO
                .builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .name(member.getName())
                .regNoBirth(member.getRegNoBirth())
                .regNoEnc(member.getRegNoEnc())
                .build();
    }
}
