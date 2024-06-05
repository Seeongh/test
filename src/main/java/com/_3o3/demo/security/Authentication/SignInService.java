package com._3o3.demo.security.Authentication;

import com._3o3.demo.api.member.domain.Member;
import com._3o3.demo.api.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.info("ash userDetailService -> loadUserByUserName {}", username);
      Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하는 사용자가 없습니다."));

        return new SignInDetails(member);
    }
}
