package com._3o3.demo.api.tax.domain;

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

    @Column(name = "calculated_tax_amount")
    private BigDecimal calculatedTaxAmount;

    @Column(name = "tax_base")
    private BigDecimal taxBase;

    @Column(name = "determined_tax_amount")
    private BigDecimal determinedTaxAmount;

    @Column(name = "tax_calculation_year")
    private LocalDate taxCalculationYear;

    @Builder
    public AnnualTax(Long id, BigDecimal calculatedTaxAmount, BigDecimal taxBase, BigDecimal determinedTaxAmount, LocalDate taxCalculationYear) {
        this.id = id;
        this.calculatedTaxAmount = calculatedTaxAmount;
        this.taxBase = taxBase;
        this.determinedTaxAmount = determinedTaxAmount;
        this.taxCalculationYear = taxCalculationYear;
    }
}
