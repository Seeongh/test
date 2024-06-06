package com._3o3.demo.user;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.domain.MemberStandard;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.api.member.infrastructure.MemberStandardRepository;
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

    @Autowired
    MemberStandardRepository memberStandardRepositoryRepository;
  @Test
  public void 회원가입_승인된_사용자() throws Exception {
      Member member = Member.builder()
              .userId("test")
              .name("동탁")
              .password("passwordTest")
              .regNoBirth("921108")
              .regNoEnc(AES256Util.encrypt("1582816"))
              .build();

      MemberStandard getMember = memberStandardRepositoryRepository.findByNameRegNo(member.getName(),member.getRegNoBirth(), member.getRegNoEnc())
              .orElse(null);

      System.out.println(getMember.toString());
      Assertions.assertThat(getMember).isInstanceOf(MemberStandard.class);

   }

    @Test
    public void 회원가입_승인되지않은_사용자() throws Exception {
        Member member = Member.builder()
                .userId("test")
                .name("test")
                .password("passwordTest")
                .regNoBirth("921108")
                .regNoEnc(AES256Util.encrypt("1582816"))
                .build();

        MemberStandard getMember = memberStandardRepositoryRepository.findByNameRegNo(member.getName(),member.getRegNoBirth(), member.getRegNoEnc())
                .orElse(null);
 ;
        Assertions.assertThat(getMember).isNull();

    }

   @Test
   public void userId가_중복된_회원가입() throws Exception {
       //given
       Member member = Member.builder()
               .userId("test")
               .name("동탁")
               .password("passwordTest")
               .regNoBirth("921108")
               .regNoEnc(AES256Util.encrypt("1582816"))
               .build();
       Member savedMember = memberRepository.save(member);

       //when
       //가입 승인된 회원이지만 id가 중복됨
       Member newMember = Member.builder()
               .userId("test")
               .name("관우")
               .password("passwordTest")
               .regNoBirth("681108")
               .regNoEnc(AES256Util.encrypt("1582816"))
               .build();
       //then
       Member getMember = memberRepository.findByUserIdOrRegNoBirthAndRegNoEnc(newMember.getUserId(), newMember.getRegNoBirth(), newMember.getRegNoEnc())
               .orElse(null);

       //관우 정보를 넣어서 동탁이나와야함(id가 같아서)
       Assertions.assertThat(getMember.getName()).isEqualTo("동탁");
       Assertions.assertThat(getMember.getName()).isEqualTo(savedMember.getName());
    }

    @Test
    public void 주민번호_중복된_회원가입() throws Exception {
        //given
        Member member = Member.builder()
                .userId("test")
                .name("동탁")
                .password("passwordTest")
                .regNoBirth("921108")
                .regNoEnc(AES256Util.encrypt("1582816"))
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        //가입 승인된 회원이지만 주민번호가 중복됨
        Member newMember = Member.builder()
                .userId("secondTest")
                .name("동탁")
                .password("passwordTest")
                .regNoBirth("921108")
                .regNoEnc(AES256Util.encrypt("1582816"))
                .build();
        //then
        Member getMember = memberRepository.findByUserIdOrRegNoBirthAndRegNoEnc(newMember.getUserId(), newMember.getRegNoBirth(), newMember.getRegNoEnc())
                .orElse(null);

        //관우 정보를 넣어서 동탁이나와야함(주민번호가 같아서)
        Assertions.assertThat(getMember.getName()).isEqualTo("동탁");
        Assertions.assertThat(getMember.getName()).isEqualTo(savedMember.getName());
    }

}