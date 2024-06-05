package com._3o3.demo.api.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;


/**
 * 가입가능한 회원 조건
 */
@Entity
@Getter
@ToString
public class MemberStandard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name ;
    private String regNoBirth;
    private String regNoEnc;
}
