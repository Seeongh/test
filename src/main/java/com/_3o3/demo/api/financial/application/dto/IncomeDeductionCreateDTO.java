package com._3o3.demo.api.financial.application.dto;

import com._3o3.demo.api.financial.domain.IncomeDeduction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class IncomeDeductionCreateDTO {

    private BigDecimal nationPensionAmount;
    private BigDecimal creditCardAmount;

    public IncomeDeduction toEntity() {
        return IncomeDeduction
                .builder()
                .nationPensionAmount(nationPensionAmount)
                .creditCardAmount(creditCardAmount)
                .totalDeductionAmount(nationPensionAmount.add(creditCardAmount))
                .build();
    }
}
