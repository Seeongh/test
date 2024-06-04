package com._3o3.demo.api.tax.domain;

import com._3o3.demo.api.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

/**
 * scrap으로 가지고온 정보
 * 종합 소득 금액
 */
@Entity
@Getter
@ToString
@NoArgsConstructor
public class AnnualFinancial {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finanial_id")
    private Long id;

    @Column(name = "annual_total_amount")
    private BigDecimal annualTotalAmount;

    @Column(name = "income_year")
    private LocalDate incomeYear;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private User user;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "incomeDeduction_id")
    private IncomeDeduction incomeDeduction;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "taxDeduction_id")
    private TaxDeduction taxDeduction;

    @Builder
    public AnnualFinancial(Long id, BigDecimal annualTotalAmount, LocalDate incomeYear, User user, IncomeDeduction incomeDeduction, TaxDeduction taxDeduction) {
        this.id = id;
        this.annualTotalAmount = annualTotalAmount;
        this.incomeYear = incomeYear;
        this.user = user;
        this.incomeDeduction = incomeDeduction;
        this.taxDeduction = taxDeduction;
    }
}
