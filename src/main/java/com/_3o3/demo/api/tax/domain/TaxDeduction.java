package com._3o3.demo.api.tax.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class TaxDeduction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taxDeduction_id")
    private Long id;

    @Column(name = "tax_credit_amount")
    private BigDecimal taxCreditAmount;

    @OneToOne(mappedBy = "taxDeduction", fetch = LAZY)
    private AnnualFinancial annualFinancial;

    @Builder
    public TaxDeduction(Long id, BigDecimal taxCreditAmount, AnnualFinancial annualFinancial) {
        this.id = id;
        this.taxCreditAmount = taxCreditAmount;
        this.annualFinancial = annualFinancial;
    }
}
