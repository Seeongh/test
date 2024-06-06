package com._3o3.demo.api.member.application.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TockenDTO {
    String accessToken ;
}
