package com._3o3.demo.security.Authentication;

import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService  implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("SignInService :: loadUserByUserName userId ashashash {}", userId);

        Member  member = memberRepository.findByUserId(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("가입하지 않은 사용자."));
        return new SignInDetails(member);
    }
}
