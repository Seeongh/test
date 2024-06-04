package com._3o3.demo.api.tax.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;

/**
 * scrap으로 가지고온 소득 공제 테이블
 * 국민연금소득공제, 신용카드소득공제, 총합 등
 */
@Entity
@Getter
@ToString
@NoArgsConstructor
public class IncomeDeduction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="income_deduction_id")
    private Long id;

    @Column(name="nation_pension_amount")
    private BigDecimal nationPensionAmount;

    @Column(name="credit_card_amount")
    private BigDecimal creditCardAmount;

    @Column(name="total_deduction_amount")
    private BigDecimal totalDeductionAmount;

    @OneToOne(mappedBy = "incomeDeduction", fetch = LAZY)
    private AnnualFinancial annualFinancial;

    @Builder
    public IncomeDeduction(Long id, BigDecimal nationPensionAmount, BigDecimal creditCardAmount, BigDecimal totalDeductionAmount, AnnualFinancial annualFinancial) {
        this.id = id;
        this.nationPensionAmount = nationPensionAmount;
        this.creditCardAmount = creditCardAmount;
        this.totalDeductionAmount = totalDeductionAmount;
        this.annualFinancial = annualFinancial;
    }


}
