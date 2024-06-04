package com._3o3.demo.user;

import com._3o3.demo.api.user.domain.User;
import com._3o3.demo.api.user.infrastructure.UserRepository;
import com._3o3.demo.util.AES256Util;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    /**
     * 1.H2-JPA 연동테스트
     * 2. 회원가입
     **/

    //@Rollback(false)
    @Test
    @Transactional
    public void testUser() throws Exception {
        //given
        User user = User.builder()
            .userId("test")
            .name("testA")
            .password("passwordTest")
                .regNoBirth("921108")
                .regNoEnc(AES256Util.encrypt("1582816"))
            .build();
        //when

        Long savedId = userRepository.save(user);
        User findUser = userRepository.findById(savedId);
        //then
        Assertions.assertThat(findUser.getId()).isEqualTo(savedId);
        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
     }
}
