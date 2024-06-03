package com._3o3.demo.security.config;

import com._3o3.demo.filter.myFilter;
import com._3o3.demo.security.Authentication.SignInService;
import com._3o3.demo.security.jwt.JwtAuthenticationFilter;
import com._3o3.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity //Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {


    private final CorsFilter corsFilter;
    private final SignInService signInService;
    private final JwtUtil jwtUtil;

    private static final String[] AUTH_WHITELIST = {
            "/szs/signup", "/szs/login", "/szs/scrap", "/szs/refund"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
               // .addFilterBefore(new myFilter(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(corsFilter)
                .sessionManagement((sessionManagement) ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtAuthenticationFilter(signInService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .formLogin(
                        AbstractHttpConfigurer::disable
                )
                .httpBasic(
                        AbstractHttpConfigurer::disable
                ).authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("*/error").permitAll()
                                .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll() // GET 요청 허용
                                .requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll() // GET 요청 허용
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated()
                );

//                .formLogin((formLogin) ->
//                        formLogin.loginPage("/login")
//                                .defaultSuccessUrl("/articles")
//                ).logout((logout) ->
//                        logout.logoutSuccessUrl("/login")
//                                .invalidateHttpSession(true)
//                );

        //H2 설정
        http
                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                );


        return http.build();
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//        //스프링 시큐리티의 인증을 담당, 사용자 인증시 앞에서 작성한 UserSecurityService 와 PasswordEncoder 를 사용
//    AuthenticationManager authenticationManager() throws Exception {
//       // return authenticationConfiguration.getAuthenticationManager();
//    }
}


