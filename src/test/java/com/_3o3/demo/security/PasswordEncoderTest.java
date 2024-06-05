package com._3o3.demo.security;

import com._3o3.demo.api.member.application.MemberService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    MemberService userService;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Test
    public void 비밀번호_암호화_테스트() throws Exception {
        // given
        String rawPassword = "12345678";

        // when
        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        // then
        Assertions.assertNotEquals(rawPassword, encodedPassword);
        Assertions.assertTrue(bCryptPasswordEncoder.matches(rawPassword, encodedPassword));
     }
}
