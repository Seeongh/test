package com._3o3.demo.security.Authentication.jwt;

import com._3o3.demo.common.exception.handler.ErrorCode;
import com._3o3.demo.security.Authentication.SignInService;
import com._3o3.demo.security.Authentication.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//userrid, password전송시
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SignInService signInService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        try {
            //JWT 헤더 있는 경우
            if((authorizationHeader != null) && (authorizationHeader.startsWith("Bearer "))) {
                String token = authorizationHeader.substring(7);
                log.info("ash jwt in {}", token);
                //JWT 유효성 검증
                if(jwtTokenProvider.validateToken(token)) {
                    String userId = jwtTokenProvider.getUserId(token);
                    log.info("ash jwt getUeserId = {}", userId);
                    //유저와 토큰 일치 시 userDetails 생성
                    UserDetails userDetails = signInService.loadUserByUsername(userId);

                    if(userDetails != null) {
                        //받아온 요청으로 접근권한 인증 Token생성
                        UsernamePasswordAuthenticationToken  usernamePasswordAuthenticationToken=
                                new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());

                        log.info("허용된 사용자 userDetails = {}", userDetails.toString());
                        //현재 Request의 Security Context에 접근 권한 설정
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }

                }
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getCode());
        }
        filterChain.doFilter(request, response);
    }
}
