package com._3o3.demo.api.member.domain;

import com._3o3.demo.api.financial.domain.AnnualFinancial;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member" , uniqueConstraints = {
        @UniqueConstraint(
                name="USER_ID_UNIQUE",
                columnNames={"user_id"}
        )})
@Getter
@ToString
@NoArgsConstructor
public class Member {

    //식별자
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @NotNull
    private Long id;

    //사용자 아이디
    @Column(name = "user_id")
    @NotNull
    private String userId;

    //사용자 비밀번호
    @Column(name = "password_enc")
    @NotNull
    private String password;

    @NotNull
    //사용자 이름
    private String name;

    //사용자 주민 등록 번호 - 생년월일
    @Column(name = "reg_no_birth")
    @NotNull
    private String regNoBirth;

    //사용자 주민 등록 번호 - 뒷자리 암호화
    @Column(name = "reg_no_enc")
    @NotNull
    private String regNoEnc;

    //권한
    @Enumerated(EnumType.STRING)
    private RoleType role;

    //가급적 양방향 연관관계가 아닌 단방향 연관관계 사용.
//    @OneToMany(mappedBy = "member") //mappedBy (여기는 거울처럼 조회용으로만 쓰겠다.)
//    private List<AnnualFinancial> annualFinancialList = new ArrayList<>();


    @Builder
    public Member(Long id, String userId, String password, String name, String regNoBirth, String regNoEnc, RoleType role ) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNoBirth = regNoBirth;
        this.regNoEnc = regNoEnc;
        this.role = role;
    }

    public void create() {
        this.role = RoleType.USER; //고정 사용
    }

}
