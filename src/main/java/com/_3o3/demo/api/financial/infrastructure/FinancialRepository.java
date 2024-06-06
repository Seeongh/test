package com._3o3.demo.api.financial.infrastructure;

import com._3o3.demo.api.financial.application.dto.AnnualFinancialAllDTO;
import com._3o3.demo.api.financial.domain.AnnualFinancial;
import com._3o3.demo.api.financial.domain.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface FinancialRepository extends JpaRepository<AnnualFinancial, Long> {

    @Query("SELECT new com._3o3.demo.api.financial.application.dto.AnnualFinancialAllDTO(af.annualTotalAmount, af.incomeYear, af.member, "
            + "id.nationPensionAmount, id.creditCardAmount, id.totalDeductionAmount, td.taxCreditAmount) "
            + "FROM AnnualFinancial af "
            + "JOIN af.incomeDeduction id "
            + "JOIN af.taxDeduction td "
            + "WHERE af.member.userId = :userId "
            + "ORDER BY af.incomeYear DESC")
    Optional<AnnualFinancialAllDTO> findByUserId(@Param("userId") String userId);

}
