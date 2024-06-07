package com._3o3.demo.service;

import com._3o3.demo.api.member.application.MemberService;
import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.api.member.infrastructure.MemberStandardRepository;
import com._3o3.demo.api.member.presentation.MemberController;
import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 회원가입
 * - 가입 권한이 없는 이름 인지 확인
 * - 가입 권한이 있지만 기 가입한 사람인지 확인
 * - 가입완료
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceSignupTest {

    @Autowired
    private MemberService memberService; // 실제 테스트하려는 서비스 객체

    private MemberCreateDTO signupRequest(String userId, String password, String name, String regNo) {
        return MemberCreateDTO.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .build();
    }

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        MemberCreateDTO requestDto = signupRequest("test", "123456", "동탁", "921108-1582816");

        //when
        String userName = memberService.signup(requestDto).getData();

        // then
        Assertions.assertThat(userName).isEqualTo(requestDto.getName());
        Assertions.assertThat(userName).isEqualTo("동탁");
     }

    @Test(expected = MethodArgumentNotValidException.class)
    public void 유효하지않은_데이터_회원가입() throws Exception {
        //given
        MemberCreateDTO requestDto = signupRequest("test", "123456", "동탁", "9211ss-1582816");

        //when
        String savedName = memberService.signup(requestDto).getData();

        //then
        fail("유호성 검증 에러 발생");

    }
    @Test(expected = CustomValidationException.class)
    public void 가입권한_없는_회원가입() throws Exception {
        //given
        MemberCreateDTO requestDto = signupRequest("test", "123456", "철수", "921109-1582816");

        //when
        String savedName = memberService.signup(requestDto).getData();
        //then
        fail("권한이 없는 사용자 가입");
    }

    @Test(expected = CustomValidationException.class)
    public void 가입권한있음_기가입_회원가입() throws Exception {
        //given
        MemberCreateDTO requestDto = signupRequest("test", "123456", "동탁", "921108-1582816");

        //when
        String savedName = memberService.signup(requestDto).getData();
        MemberCreateDTO sameDto = signupRequest("test2", "123456", "동탁", "921108-1582816");

        memberService.signup(sameDto);
        //then
        fail("예외발생");
    }
}
