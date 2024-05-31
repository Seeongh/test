package com._3o3.demo.user;

import com._3o3.demo.api.domain.User;
import com._3o3.demo.api.infrastructure.UserRepository;
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


    /*
     H2-JPA 연동테스트
     */

    //@Rollback(false)
    @Test
    @Transactional
    public void testUser() throws Exception {
        //given
        User user = User.builder()
            .userId("test")
            .name("testA")
            .password("passwordTest")
            .regNo("YYMMDD-GABCDEF")
            .build();
        //when

        Long savedId = userRepository.save(user);
        User findUser = userRepository.find(savedId);
        //then
        Assertions.assertThat(findUser.getId()).isEqualTo(savedId);
        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
     }
}
