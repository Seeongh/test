package com._3o3.demo.security.Authentication.provider;

import com._3o3.demo.api.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final Long accessTokenExpTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") Long accessTokenExpTime) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Member member) {
        log.info("jwtTokenProvider createAccessToken");
        return createToken(member, accessTokenExpTime);
    }

    private String createToken(Member member, Long accessTokenExpTime) {
        log.info("jwtTokenProvider createToken");

        Claims claims = Jwts.claims();
        claims.put("userId", member.getUserId());
        claims.put("name", member.getName());
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token에서 userId 추출
     */
    public String getUserId(String token) {
        return parseClaims(token).get("userId", String.class);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * JWT 검증
     */
    public boolean validateToken(String token) {
        log.info("jwtTokenProvider validateToken");
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
    }
}
