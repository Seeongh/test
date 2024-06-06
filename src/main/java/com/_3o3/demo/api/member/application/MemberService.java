package com._3o3.demo.api.member.application;


import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

        validationDuplicateType(member); // 중복 및 오타 검사

        //DB 전달
        memberRepository.save(member);
        Long result = 1L;

        return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
                : ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }


    private void validationDuplicateType(Member member) {
        memberRepository.findByUserId(member.getUserId())
                .ifPresent(u -> { throw new CustomValidationException("이미 가입한 사용자가 있습니다."); });
    }

    @Transactional
    public ApiResponse<TockenDTO> login(MemberSignInDTO signInDto) {
        log.info("ash userservice" + signInDto.getUserId());
        //존재하는 Id인지, pw가 맞는지 여부 확인
       LoginAuthenticationVerification(signInDto);

        Member member = signInDto.toEntity();
        //UsernamePasswordAuthenticationToken autenticationToken = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());

        //실제 검증(사용자 비밀번호 체크)
        // detailService -> loadUserByUsername 메서드 실행
        //Authentication authentication = authenticationManagerBuilder.getObject().authenticate(autenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(member);
        TockenDTO tockenDTO = TockenDTO.builder()
                            .accessToken(accessToken)
                            .build();
        return  ApiResponse.of(HttpStatus.OK, tockenDTO);
    }

    private void LoginAuthenticationVerification(MemberSignInDTO signInDTO) {
        //회원 존재 여부 확인
        Member findMember = memberRepository.findByUserId(signInDTO.getUserId())
                .orElseThrow( () -> new UsernameNotFoundException("존재하는 사용자가 없습니다."));

        //비밀번호 매치 확인
        if(!bCryptPasswordEncoder.matches(signInDTO.getPassword(), findMember.getPassword() )) {
             throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

}
