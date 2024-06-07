package com._3o3.demo.api.member.application;


import com._3o3.demo.api.member.application.dto.MemberCreateDTO;
import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.application.dto.TockenDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import com._3o3.demo.api.member.infrastructure.MemberStandardRepository;
import com._3o3.demo.common.ApiResultResponse;
import com._3o3.demo.common.exception.CustomValidationException;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
@Validated
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberStandardRepository memberStandardRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원 가입
     * @param 회원 가입 객체
     * @return ApiResponse.of(
     *                  HttpStatus : 성공여부
     *                  data : 사용자 이름
     */
    @Transactional
    public ApiResultResponse<String> signup(@Valid MemberCreateDTO createDto) {
        //DTO -> Entity 로 변경
        //비밀번호, 주소 암호화
        Member member = createDto.toEntity(bCryptPasswordEncoder);
        member.create(); //role부여

        validationJoinAuthorization(member); // 중복 및 오타 검사

        //DB 전달
        Member getMember = memberRepository.save(member);

        return getMember != null ? ApiResultResponse.of(HttpStatus.OK, getMember.getName())
                : ApiResultResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    /**
     * 가입 권한 여부 확인 및 중복 가입 방지
     * @param member
     */
    private void validationJoinAuthorization(@Valid Member member) {
        // 가입 가능한 회원인지 확인
        memberStandardRepository.findByNameRegNo(member.getName(), member.getRegNoBirth(), member.getRegNoEnc())
                .orElseThrow( () -> new CustomValidationException("가입 권한이 없습니다."));

        // 해당 ID로 가입 가능한지 확인
        memberRepository.findByUserIdOrRegNoBirthAndRegNoEnc(member.getUserId(), member.getRegNoBirth(), member.getRegNoEnc())
                .ifPresent(m -> {
                    throw new CustomValidationException(HttpStatus.CONFLICT, "이미 가입한 사용자가 있습니다.");
                });
    }

    /**
     * 로그인
     * @param signInDto
     * @return
     */
    @Transactional
    public ApiResultResponse<TockenDTO> login(@Valid MemberSignInDTO signInDto) {
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
        return  ApiResultResponse.of(HttpStatus.OK, tockenDTO);
    }
}
