package com._3o3.demo.security.Authentication.provider;

import com._3o3.demo.api.user.domain.User;
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
    public String createAccessToken(User user) {
        log.info("jwtTokenProvider createAccessToken");
        return createToken(user, accessTokenExpTime);
    }

    private String createToken(User user, Long accessTokenExpTime) {
        log.info("jwtTokenProvider createToken");

        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("role", user.getRole());

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

        log.info("jwtTokenProvider getUserId  token : {}", token);
        return parseClaims(token).get("userId", String.class);
    }

    private Claims parseClaims(String accessToken) {

        log.info("jwtTokenProvider parseClaims accessToken ={}", accessToken);
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
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new UnsupportedJwtException("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new UnsupportedJwtException("JWT claims string is empty");
        } catch (Exception e) {
            throw new UnsupportedJwtException("JWT Unkown Error");
        }

        return false;
    }
}
