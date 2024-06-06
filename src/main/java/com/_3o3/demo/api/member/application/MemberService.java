package com._3o3.demo.api.member.application;


import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.domain.MemberStandard;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.api.member.infrastructure.MemberStandardRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class MemberService {

    //유저 저장공간
    private final MemberRepository memberRepository;
    private final MemberStandardRepository memberStandardRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원 가입
     * @param 회원 가입 객체
     * @return
     */
    @Transactional
    public ApiResponse<Long> join(MemberCreateDTO createDto) {

        //DTO -> Entity 로 변경
        //비밀번호, 주소 암호화
        Member member = createDto.toEntity(bCryptPasswordEncoder);
        member.create(); //role부여

        validationJoinAuthorization(member); // 중복 및 오타 검사

        //DB 전달
        memberRepository.save(member);
        Long result = 1L;

        return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
                : ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }


    private void validationJoinAuthorization(Member member) {
        // 가입 가능한 회원인지 확인
        memberStandardRepository.findByNameRegNo(member.getName(), member.getRegNoBirth(), member.getRegNoEnc())
                .orElseThrow( () -> new CustomValidationException("가입 권한이 없습니다."));

        // 해당 ID로 가입 가능한지 확인
        memberRepository.findByUserIdOrRegNoBirthAndRegNoEnc(member.getUserId(), member.getRegNoBirth(), member.getRegNoEnc())
                .ifPresent(m -> {
                    throw new CustomValidationException("이미 가입한 사용자가 있습니다.");
                });
    }

    @Transactional
    public ApiResponse<TockenDTO> login(MemberSignInDTO signInDto) {
        Member member = signInDto.toEntity();
        UsernamePasswordAuthenticationToken autenticationToken = new UsernamePasswordAuthenticationToken(member.getUserId(), member.getPassword());
        //실제 검증(사용자 비밀번호 체크)
        // detailService -> loadUserByUsername 메서드 실행
        log.info("MemberService login :: call customUserAuthenticationProvider");
         authenticationManagerBuilder.getObject().authenticate(autenticationToken);


        log.info("MemberService login :: createToken");
        String accessToken = jwtTokenProvider.createAccessToken(member);
        TockenDTO tockenDTO = TockenDTO.builder()
                            .accessToken(accessToken)
                            .build();
        return  ApiResponse.of(HttpStatus.OK, tockenDTO);
    }



}
