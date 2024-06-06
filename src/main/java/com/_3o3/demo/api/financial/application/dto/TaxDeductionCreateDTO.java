package com._3o3.demo.api.financial.application.dto;

import com._3o3.demo.api.financial.domain.TaxDeduction;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaxDeductionCreateDTO {

    private BigDecimal taxCreditAmount;

    public TaxDeduction toEntity() {
        return TaxDeduction
                .builder()
                .taxCreditAmount(taxCreditAmount)
                .build();
    }

}
