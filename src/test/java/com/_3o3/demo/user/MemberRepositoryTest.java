package com._3o3.demo.user;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.api.member.infrastructure.MemberRepositoryImpl;
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
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    /**
     * 1.H2-JPA 연동테스트
     * 2. 회원가입
     **/

    //@Rollback(false)
    @Test
    @Transactional
    public void testUser() throws Exception {
        //given
        Member member = Member.builder()
            .userId("test")
            .name("testA")
            .password("passwordTest")
                .regNoBirth("921108")
                .regNoEnc(AES256Util.encrypt("1582816"))
            .build();
        //when

        //Long savedId = memberRepository.save(member);
        //Member findMember = memberRepository.findById(savedId);
        //then
     //   Assertions.assertThat(findMember.getId()).isEqualTo(savedId);
      //  Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
     }

     @Test
     public void 암호화_테스트() throws Exception {
         //given
         System.out.println("ash aes =" +AES256Util.encrypt("1582816"));
         System.out.println("ash aes =" +AES256Util.encrypt("1582816"));
         System.out.println("ash aes =" +AES256Util.encrypt("2455116"));
         System.out.println("ash aes =" +AES256Util.encrypt("1656116"));
         System.out.println("ash aes =" +AES256Util.encrypt("2715702"));





         //when

         //then

      }

      @Test
      public void repository_테스트() throws Exception {
          //given
          Member member = Member.builder()
                  .userId("test")
                  .name("testA")
                  .password("passwordTest")
                  .regNoBirth("921108")
                  .regNoEnc(AES256Util.encrypt("1582816"))
                  .build();
          //when
          memberRepository.save(member);
          String password = String.valueOf(memberRepository.findByUserId("test"));
          System.out.println(password);

          //then

       }
}
//@SpringBootTest
//@Transactional
//@Rollback(false)
//class UserRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    void findUsernameAndAddressTest() {
//
//        // given
//        String jamesUsername = "James";
//        String maryUsername = "Mary";
//
//        User james = User.builder()
//                .username(jamesUsername)
//                .age(25)
//                .build();
//
//        User mary = User.builder()
//                .username(maryUsername)
//                .age(30)
//                .build();
//
//        userRepository.save(james);
//        userRepository.save(mary);
//
//        // when
//        List<User> optionalUsers = userRepository.findByLetterWithConditions('a', 23);
//
//        // then
//        assertEquals(optionalUsers.get(0).getUsername(), jamesUsername);
//        assertEquals(optionalUsers.get(1).getUsername(), maryUsername);
//    }
//}