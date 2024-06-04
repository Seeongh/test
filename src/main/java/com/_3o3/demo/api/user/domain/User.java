package com._3o3.demo.api.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@SequenceGenerator(
//        name = "USER_SEQ_GENERATOR",
//        sequenceName = "USER_SEQ",
//        initialValue = 1, allocationSize = 1
//)
@Table(name = "USER_TABLE" , uniqueConstraints = {
        @UniqueConstraint(
                name="USER_ID_UNIQUE",
                columnNames={"user_id"}
        )})
@Getter
@ToString
@NoArgsConstructor
public class User {

    //식별자
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 아이디
    @Column(name = "user_id")
    private String userId;

    //사용자 비밀번호
    @Column(name = "password_enc")
    private String password;

    //사용자 이름
    private String name;

    //사용자 주민 등록 번호 - 생년월일
    @Column(name = "reg_no_birth")
    private String regNoBirth;

    //사용자 주민 등록 번호 - 뒷자리 암호화
    @Column(name = "reg_no_enc")
    private String regNoEnc;

    //권한
    @Enumerated(EnumType.STRING)
    private RoleType role;


    @Builder
    public User(Long id, String userId, String password, String name, String regNoBirth, String regNoEnc, RoleType role) {
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