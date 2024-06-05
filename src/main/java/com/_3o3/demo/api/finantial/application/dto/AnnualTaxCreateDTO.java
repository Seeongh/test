package com._3o3.demo.api.finantial.application.dto;

import com._3o3.demo.api.finantial.domain.AnnualTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class AnnualTaxCreateDTO {
    private BigDecimal calculatedTaxAmount;
    private BigDecimal taxBase;
    private BigDecimal determinedTaxAmount;
    private LocalDate taxCalculationYear;

    public AnnualTax toEntity() {
        return AnnualTax
                .builder()
                .calculatedTaxAmount(calculatedTaxAmount)
                .taxBase(taxBase)
                .determinedTaxAmount(determinedTaxAmount)
                .taxCalculationYear(taxCalculationYear)
                .build();
    }
}
