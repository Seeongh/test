package com._3o3.demo.api.financial.domain;

import com._3o3.demo.api.member.domain.Member;
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
    private Member member;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "incomeDeduction_id")
    private IncomeDeduction incomeDeduction;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "taxDeduction_id")
    private TaxDeduction taxDeduction;

    @Builder
    public AnnualFinancial(Long id, BigDecimal annualTotalAmount, LocalDate incomeYear, Member member, IncomeDeduction incomeDeduction, TaxDeduction taxDeduction) {
        this.id = id;
        this.annualTotalAmount = annualTotalAmount;
        this.incomeYear = incomeYear;
        this.member = member;
        this.incomeDeduction = incomeDeduction;
        this.taxDeduction = taxDeduction;
    }

    public void create(Member member, IncomeDeduction incomeDeduction, TaxDeduction taxDeduction ) {
        this.member = member;
        this.incomeDeduction = incomeDeduction;
        this.taxDeduction = taxDeduction;
    }
}
