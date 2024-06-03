package com._3o3.demo.security.jwt;

import com._3o3.demo.security.Authentication.SignInDetails;
import com._3o3.demo.security.Authentication.SignInService;
import com._3o3.demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//userrid, password전송시
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SignInService signInService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        //JWT 헤더에 있는경우
        if((authorizationHeader != null) && (authorizationHeader.startsWith("Bearer "))) {
            String token = authorizationHeader.substring(7);
            //JWT 유효성 검증
            if(jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserId(token);

                //유저와 토큰 일치 시 userDetails 생성
                UserDetails userDetails = signInService.loadUserByUsername(userId);

                if(userDetails != null) {
                    //받아온 요청으로 접근권한 인증 Token생성
                    UsernamePasswordAuthenticationToken  usernamePasswordAuthenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails, null);

                    //현재 Request의 Security Context에 접근 권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
        }

        filterChain.doFilter(request, response);
    }
}
