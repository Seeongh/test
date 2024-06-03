package com._3o3.demo.util;

import com._3o3.demo.api.domain.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

/**
 * jwt관련 메서드 제공
 */

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final Long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") Long accessTokenExpTime) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("ash key: {}", key);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(User user) {
        return createToken(user, accessTokenExpTime);
    }

    private String createToken(User user, Long accessTokenExpTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());

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
          return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(accessToken).getBody();
      } catch (ExpiredJwtException e) {
          return e.getClaims();
      }
    }

    /**
     * JWT 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch ( io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
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
