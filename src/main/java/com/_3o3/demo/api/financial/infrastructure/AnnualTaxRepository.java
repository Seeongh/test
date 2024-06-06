package com._3o3.demo.api.financial.infrastructure;

import com._3o3.demo.api.financial.domain.AnnualTax;
import com._3o3.demo.api.financial.domain.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualTaxRepository extends JpaRepository<AnnualTax, Long> {
}
