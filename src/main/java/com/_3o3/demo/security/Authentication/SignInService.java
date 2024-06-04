package com._3o3.demo.security.Authentication;

import com._3o3.demo.api.user.domain.User;
import com._3o3.demo.api.user.infrastructure.UserRepository;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.info("ash userDetailService -> loadUserByUserName {}", username);
      User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하는 사용자가 없습니다."));

        return new SignInDetails(user);
    }
}
