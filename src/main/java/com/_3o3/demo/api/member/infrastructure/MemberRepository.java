package com._3o3.demo.api.member.infrastructure;

import com._3o3.demo.api.financial.domain.TaxRate;
import com._3o3.demo.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE userId = :userId")
    Optional<Member> findByUserId(@Param("userId") String userId);

}
