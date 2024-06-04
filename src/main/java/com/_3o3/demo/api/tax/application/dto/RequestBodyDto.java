package com._3o3.demo.api.tax.application.dto;

import com._3o3.demo.api.user.domain.User;
import com._3o3.demo.util.AES256Util;
import lombok.*;
import org.apache.coyote.Request;

import javax.naming.Name;


@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestBodyDto {
    private String name;
    private String regNo;

    public RequestBodyDto of(User user) {
        return RequestBodyDto
                .builder()
                .name(user.getName())
                .regNo(user.getRegNoBirth()+"-"+ AES256Util.decrypt(user.getRegNoEnc()))
                .build();
    }
}
