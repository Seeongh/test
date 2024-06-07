package com._3o3.demo.service;

import com._3o3.demo.api.member.application.MemberService;
import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.common.exception.CustomValidationException;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.fail;

/**
 * 로그인 테스트
 * - RequestBody 파라미터들 유효성 검사(필수)
 * - 가입한 사용자인지 아닌지 확인
 * - 비밀번호 일치 여부 확인
 * - 로그인 완료
 */

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MemberServiceLoginTest {

    @Autowired
    private MemberService memberService; // 실제 테스트하려는 서비스 객체
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void before() {
        MemberCreateDTO createDto =  MemberCreateDTO.builder()
                .userId("test")
                .password("test12")
                .name("동탁")
                .regNo("921108-1582816")
                .build();

        memberRepository.save(createDto.toEntity(bCryptPasswordEncoder));
    }

    private MemberSignInDTO loginRequest(String userId, String password) {
        return MemberSignInDTO
                .builder()
                .userId(userId)
                .password(password)
                .build();
    }

    @Test
    public void 로그인_비밀번호일치_성공() throws Exception {
        //given
        String rawPassword = "test12";
        MemberSignInDTO requestDto = loginRequest("test", rawPassword);

        // then;
        //when
        HttpStatus httpStatus= memberService.login(requestDto).getHttpStatus();

        // then
        Assertions.assertThat(httpStatus).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(bCryptPasswordEncoder.matches(rawPassword, bCryptPasswordEncoder.encode(rawPassword))). isTrue();
    }

    @Test(expected = ConstraintViolationException.class)
    public void 유효하지않은_데이터_로그인() throws Exception {
        //given
        MemberSignInDTO requestDto = loginRequest(" ", "test12");

        //when
         memberService.login(requestDto);

        //then
        fail("유호성 검증 에러 발생");

    }

    @Test(expected = UsernameNotFoundException.class)
    public void 가입하지_않은_사용자() throws Exception {
        //given
        MemberSignInDTO requestDto = loginRequest("test2", "test12");

        //when
        memberService.login(requestDto);

        //then
        fail("가입하지 않은 사용자로 로그인 안됨.");

    }

    @Test(expected = BadCredentialsException.class)
    public void 비밀번호_오류() throws Exception {
        //given
        MemberSignInDTO requestDto = loginRequest("test", "test123");

        //when
        memberService.login(requestDto);

        //then
        fail("비밀번호 오류로 로그인 안됨.");

    }
}
