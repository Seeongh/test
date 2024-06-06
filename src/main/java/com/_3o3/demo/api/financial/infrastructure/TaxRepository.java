package com._3o3.demo.api.financial.infrastructure;

import com._3o3.demo.api.financial.domain.AnnualTax;
import com._3o3.demo.api.financial.domain.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TaxRepository  extends JpaRepository<TaxRate, Long> {
    Optional<TaxRate> findFirstByTaxStandardLessThanEqualOrderByTaxStandardDesc(BigDecimal taxIncome);
}
