package com._3o3.demo.api.financial.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Column(name = "calculated_tax")
    private BigDecimal calculatedTax;

    //과세표준
    @Column(name = "tax_base")
    private BigDecimal taxIncome;

    //결정세액
    @Column(name = "final_tax_amount")
    private BigDecimal finalTaxAmount;


    @Column(name = "tax_calculation_year")
    private LocalDate taxCalculationYear;

    @Builder
    public AnnualTax(Long id, BigDecimal calculatedTax, BigDecimal taxIncome, BigDecimal finalTaxAmount, LocalDate taxCalculationYear) {
        this.id = id;
        this.calculatedTax = calculatedTax;
        this.taxIncome = taxIncome;
        this.finalTaxAmount = finalTaxAmount;
        this.taxCalculationYear = taxCalculationYear;
    }
}
