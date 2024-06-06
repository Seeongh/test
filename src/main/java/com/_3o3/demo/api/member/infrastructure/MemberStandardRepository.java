package com._3o3.demo.api.member.infrastructure;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.domain.MemberStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberStandardRepository extends JpaRepository<MemberStandard, Long> {
    @Query("SELECT ms FROM MemberStandard ms WHERE name = :name and regNoBirth = :regNoBirth and regNoEnc = :regNoEnc")
    Optional<MemberStandard> findByNameRegNo(@Param("name") String name,
                                     @Param("regNoBirth") String regNoBirth,
                                     @Param("regNoEnc") String regNoEnc);

}
