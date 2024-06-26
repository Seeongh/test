package com._3o3.demo.api.financial.application.dto;

import com._3o3.demo.api.financial.domain.AnnualFinancial;
import com._3o3.demo.api.financial.domain.IncomeDeduction;
import com._3o3.demo.api.financial.domain.TaxDeduction;
import com._3o3.demo.api.member.domain.Member;
import lombok.*;

import java.math.BigDecimal;
import java.time.Year;

/**
 * scrap으로 가지고온 정보
 * 종합 소득 금액
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnualFinancialAllDTO {


    private BigDecimal annualTotalAmount;
    private Year incomeYear;
    private Member member;
    //소득공제
    private BigDecimal nationPensionAmount;
    private BigDecimal creditCardAmount;
    private BigDecimal totalDeductionAmount;

    //세액공제
    private BigDecimal taxCreditAmount;


    public AnnualFinancial toAnnualFinancialEntity() {
        return AnnualFinancial
                .builder()
                .annualTotalAmount(annualTotalAmount)
                .incomeYear(incomeYear)
                .build();
    }

    public IncomeDeduction toIncomeDeductionEntity() {
        return IncomeDeduction
                .builder()
                .nationPensionAmount(nationPensionAmount)
                .creditCardAmount(creditCardAmount)
                .totalDeductionAmount(nationPensionAmount.add(creditCardAmount))
                .build();
    }

    public TaxDeduction toTaxDeductionEntity() {
        return TaxDeduction
                .builder()
                .taxCreditAmount(taxCreditAmount)
                .build();
    }


}
