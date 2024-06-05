package com._3o3.demo.api.finantial.application.dto.webClientDto;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.util.AES256Util;
import lombok.*;


@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyDto {
    private String name;
    private String regNo;

    public RequestBodyDto of(Member member) {
        return RequestBodyDto
                .builder()
                .name(member.getName())
                .regNo(member.getRegNoBirth()+"-"+ AES256Util.decrypt(member.getRegNoEnc()))
                .build();
    }
}
