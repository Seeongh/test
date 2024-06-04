package com._3o3.demo.api.tax.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "TAX_RATE")
@Getter
public class TaxRate {
    //기본 세액 테이블 식별자
    @Id @GeneratedValue
    @Column(name = "tax_rate_id")
    private Long id;

    //과세 표준
    @Column(name = "tax_standart")
    private Integer taxStandart;

    //부여되는 금액
    @Column(name = "additional_amount")
    private Integer additionalAmount;

    //초과 금액 기준
    @Column(name = "excess_amount_threshold")
    private Integer excessAmountThreshold;

    //세율 퍼센트
    @Column(name = "tax_rate_percentage")
    private Integer taxRatePercentage;
}
