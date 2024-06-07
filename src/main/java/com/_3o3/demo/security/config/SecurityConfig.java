package com._3o3.demo.security.config;

import com._3o3.demo.security.Authentication.SignInService;
import com._3o3.demo.security.Authentication.filter.JwtAuthenticationFilter;
import com._3o3.demo.security.Authentication.provider.CustomUserAuthenticationProvider;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import com._3o3.demo.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity //Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {


    private final CorsFilter corsFilter;
    private final SignInService signInService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] ALLOW_LIST = {
            "/szs/signup", "/szs/login", "/", "/3o3/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
    };
    private static final String[] AUTH_WHITELIST = {
            "/szs/scrap", "/szs/refund"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(corsFilter)
                //jwt사용
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAuthenticationFilter(signInService, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(
                        AbstractHttpConfigurer::disable
                )
                .httpBasic(
                        AbstractHttpConfigurer::disable
                ).authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("*/error").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll() //h2허용
                        .requestMatchers(ALLOW_LIST).permitAll() // 로그인 안해도 진입가능
                        .requestMatchers(AUTH_WHITELIST).authenticated() //
                        .anyRequest().authenticated()
                )
               .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        http
                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        CustomUserAuthenticationProvider provider =
                new CustomUserAuthenticationProvider(signInService, bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }
}


