package com._3o3.demo.api.finantial.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 개인의 연간 세금 계산 테이블
 * 산출세액, 과세표준, 결정세액, 년도 등
 */
@Getter
@Builder
@AllArgsConstructor
public class AnnualTaxDto {

    private BigDecimal calculatedTaxAmount;
    private BigDecimal taxBase;
    private BigDecimal determinedTaxAmount;
    private LocalDate taxCalculationYear;

}
