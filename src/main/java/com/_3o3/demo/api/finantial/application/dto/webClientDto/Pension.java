package com._3o3.demo.api.finantial.application.dto.webClientDto;

import com._3o3.demo.util.CustomBigDecimalDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Pension {
    @JsonProperty("월")
    private String month;

    @JsonProperty("공제액")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal deductionAmount;

    // Getters and Setters
}
