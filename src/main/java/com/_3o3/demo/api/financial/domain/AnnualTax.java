package com._3o3.demo.api.financial.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

/**
 * 개인의 연간 세금 계산 테이블
 * 산출세액, 과세표준, 결정세액, 년도 등
 */
@Entity
@Getter
@ToString
@NoArgsConstructor
public class AnnualTax {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annual_tax_id")
    private Long id;

    //산출세액
    private BigDecimal calculatedTax;

    //과세표준
    private BigDecimal taxIncome;

    //결정세액
    private BigDecimal finalTaxAmount;

    private Year taxCalculationYear;

    @Builder
    public AnnualTax(Long id, BigDecimal calculatedTax, BigDecimal taxIncome, BigDecimal finalTaxAmount, Year taxCalculationYear) {
        this.id = id;
        this.calculatedTax = calculatedTax;
        this.taxIncome = taxIncome;
        this.finalTaxAmount = finalTaxAmount;
        this.taxCalculationYear = taxCalculationYear;
    }
}
