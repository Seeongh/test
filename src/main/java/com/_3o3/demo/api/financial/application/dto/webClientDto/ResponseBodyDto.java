package com._3o3.demo.api.financial.application.dto.webClientDto;

import lombok.Getter;

@Getter
public class ResponseBodyDto {
    private String status;
    private FinancialData data;
    private ResponseErrors errors;
}
