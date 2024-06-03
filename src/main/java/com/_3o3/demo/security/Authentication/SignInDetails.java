package com._3o3.demo.security.Authentication;

import com._3o3.demo.api.application.dto.UserSignInDTO;
import com._3o3.demo.api.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SignInDetails implements UserDetails {

    private final User user;

    public SignInDetails( User user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<String> authorities = new ArrayList<>();
//        signInDto.getRoleList().forEach( r-> {
//            authorities.add(()->r);
//        });
//        return authorities;
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }
}
