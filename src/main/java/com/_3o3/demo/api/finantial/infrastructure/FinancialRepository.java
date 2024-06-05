package com._3o3.demo.api.finantial.infrastructure;

import com._3o3.demo.api.finantial.application.dto.AnnualFinancialAllDTO;
import com._3o3.demo.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinancialRepository extends JpaRepository<Member, Long> {

    @Query("SELECT new com.example.dto.AnnualFinancialAllDTO(af.annualTotalAmount, af.incomeYear, af.member, "
            + "id.nationPensionAmount, id.creditCardAmount, id.totalDeductionAmount, td.taxCreditAmount) "
            + "FROM AnnualFinancial af "
            + "JOIN af.incomeDeduction id "
            + "JOIN af.taxDeduction td "
            + "WHERE af.member.id = :userId "
            + "ORDER BY af.incomeYear DESC")
    AnnualFinancialAllDTO findByUserId(@Param("userId") String userId);
}
