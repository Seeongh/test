package com._3o3.demo.api.finantial.application.dto;

import com._3o3.demo.api.finantial.domain.AnnualFinancial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class AnnualFinancialCreateDTO {

    private BigDecimal annualTotalAmount;
    private LocalDate incomeYear;

    public AnnualFinancial toEntity() {
        return AnnualFinancial
                .builder()
                .annualTotalAmount(annualTotalAmount)
                .incomeYear(incomeYear)
                .build();
    }
}
