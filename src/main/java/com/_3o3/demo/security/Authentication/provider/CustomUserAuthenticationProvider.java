package com._3o3.demo.security.Authentication.provider;
import java.util.Collection;

import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.security.Authentication.SignInDetails;
import com._3o3.demo.security.Authentication.SignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Spring Security Login을 처리하는 AuthenticationProvider 구현체.
 */
@Slf4j
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    private final SignInService signInService;
    private final PasswordEncoder passwordEncoder;

    public CustomUserAuthenticationProvider(SignInService signInService, PasswordEncoder passwordEncoder) {
        this.signInService = signInService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 인증 처리 메소드.
     * /login 진입 시 수행/
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken =
                (UsernamePasswordAuthenticationToken) authentication;

        String username = authToken.getName();
        String password = (String) authToken.getCredentials();
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("ID를 입력해주세요."); // TODO message properties 적용
        }
        if (password == null || password.isEmpty()) {
            throw new UsernameNotFoundException("비밀번호를 입력해주세요."); // TODO message properties 적용
        }

        // DB에 등록된 사용자 정보 조회
        SignInDetails signInDetails =
                (SignInDetails) signInService.loadUserByUsername(username);

        // pw 인증
        String pswdEncpt = signInDetails.getPassword();
        verifyCredentials(password, pswdEncpt);

        Collection<? extends GrantedAuthority> authorities = signInDetails.getAuthorities();

        authToken = UsernamePasswordAuthenticationToken
                .authenticated(signInDetails.getUsername(), pswdEncpt, authorities);

        authToken.setDetails(signInDetails);

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    private void verifyCredentials(String credentials, String password) {
        if (!passwordEncoder.matches(credentials, password)) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다."); // TODO message properties 적용
        }
    }

}
