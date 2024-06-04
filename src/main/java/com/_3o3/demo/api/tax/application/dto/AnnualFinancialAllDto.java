package com._3o3.demo.api.tax.application.dto;

import com._3o3.demo.api.tax.domain.AnnualFinancial;
import com._3o3.demo.api.tax.domain.IncomeDeduction;
import com._3o3.demo.api.tax.domain.TaxDeduction;
import com._3o3.demo.api.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * scrap으로 가지고온 정보
 * 종합 소득 금액
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnualFinancialAllDto {

    private BigDecimal annualTotalAmount;
    private LocalDate incomeYear;
    private User user;
    //소득공제
    private  BigDecimal nationPensionAmount;
    private BigDecimal creditCardAmount;

    //세액공제
    private BigDecimal taxCreditAmount;

    AnnualFinancial toAnnualFinancialEntity() {
        return AnnualFinancial
                .builder()
                .annualTotalAmount(annualTotalAmount)
                .incomeYear(incomeYear)
                .build();
    }

    IncomeDeduction toIncomeDeductionEntity() {
        return IncomeDeduction
                .builder()
                .nationPensionAmount(nationPensionAmount)
                .creditCardAmount(creditCardAmount)
                .build();
    }

    TaxDeduction toTaxDeductionEntity() {
        return TaxDeduction
                .builder()
                .taxCreditAmount(taxCreditAmount)
                .build();
    }


}
