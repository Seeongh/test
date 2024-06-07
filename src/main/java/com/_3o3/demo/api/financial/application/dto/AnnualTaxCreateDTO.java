package com._3o3.demo.api.financial.application.dto;

import com._3o3.demo.api.financial.domain.AnnualTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Year;

@Builder
@Getter
@AllArgsConstructor
public class AnnualTaxCreateDTO {
    //산출세액
    private BigDecimal calculatedTax;

    //과세표준
    private BigDecimal taxIncome;

    //결정세액
    private BigDecimal finalTaxAmount;

    private Year taxCalculationYear;

    public AnnualTax toEntity() {
        return AnnualTax
                .builder()
                .calculatedTax(calculatedTax)
                .taxIncome(taxIncome)
                .finalTaxAmount(finalTaxAmount)
                .taxCalculationYear(taxCalculationYear)
                .build();
    }

}
