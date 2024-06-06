package com._3o3.demo.api.financial.application.dto.webClientDto;

import com._3o3.demo.util.CustomBigDecimalDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Deduction {
    @JsonProperty("국민연금")
    private List<Pension> pension;
    @JsonProperty("신용카드소득공제")
    private CreditCardDeduction creditCardDeduction;
    @JsonProperty("세액공제")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal taxDeduction;
}
