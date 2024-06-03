package com._3o3.demo.security.Authentication;

import com._3o3.demo.api.application.dto.UserSignInDTO;
import com._3o3.demo.api.domain.User;
import com._3o3.demo.api.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignInService  implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = Optional.ofNullable(userRepository.findByUserName(username))
                .orElseThrow(() -> new UsernameNotFoundException("존재하는 사용자가 없습니다."));

        return new SignInDetails(user);
    }
}
