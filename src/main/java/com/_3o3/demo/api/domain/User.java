package com._3o3.demo.api.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(
        name = "USER_SEQ_GENERATOR",
        sequenceName = "USER_SEQ",
        initialValue = 1, allocationSize = 1
)
@Table(name = "USER_TABLE" , uniqueConstraints = {
        @UniqueConstraint(
                name="REG_NO_UNIQUE",
                columnNames={"reg_no"}
        )})
@Getter
public class User {

    //식별자
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                generator = "USER_SEQ_GENERATOR")
    private Long id;

    //사용자 아이디
    @Column(name = "user_id")
    private String userId;

    //사용자 비밀번호
    @Column(name = "password_enc")
    private String password;

    //사용자 이름
    private String name;

    //사용자 주민 등록 번호
    @Column(name = "reg_no_enc")
    private String regNo;

    @Builder
    public User(Long id, String userId, String password, String name, String regNo) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }
}
