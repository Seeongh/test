package com._3o3.demo.api.finantial.application.dto.webClientDto;

import com._3o3.demo.util.CustomBigDecimalDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FinancialData {
    @JsonProperty("종합소득금액")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal totalIncome;
    @JsonProperty("이름")
    private String name;
    @JsonProperty("소득공제")
    private Deduction deduction;

}
