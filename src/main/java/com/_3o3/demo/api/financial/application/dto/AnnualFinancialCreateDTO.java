package com._3o3.demo.api.financial.application.dto;

import com._3o3.demo.api.financial.domain.AnnualFinancial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Year;

@Builder
@Getter
@AllArgsConstructor
public class AnnualFinancialCreateDTO {

    private BigDecimal annualTotalAmount;
    private Year incomeYear;

    public AnnualFinancial toEntity() {
        return AnnualFinancial
                .builder()
                .annualTotalAmount(annualTotalAmount)
                .incomeYear(incomeYear)
                .build();
    }


}
