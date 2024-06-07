package com._3o3.demo.security.Authentication.filter;

import com._3o3.demo.common.exception.handler.ErrorCode;
import com._3o3.demo.security.Authentication.SignInDetails;
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
                log.info("jwtToken validation start");
                //JWT 유효성 검증
                if(jwtTokenProvider.validateToken(token)) {
                    log.info("jwtToken validation end");
                    String userId = jwtTokenProvider.getUserId(token);

                    //유저와 토큰 일치 시 userDetails 생성
                    // DB에 등록된 사용자 정보 조회
                    SignInDetails signInDetails =
                            (SignInDetails) signInService.loadUserByUsername(userId);
                    if(signInDetails != null) {
                        //받아온 요청으로 접근권한 인증 Token생성
                        UsernamePasswordAuthenticationToken  usernamePasswordAuthenticationToken=
                                new UsernamePasswordAuthenticationToken(signInDetails, token, signInDetails.getAuthorities());


                        //현재 Request의 Security Context에 접근 권한 설정
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }

                }
            }
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getMessage());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}