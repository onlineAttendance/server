package com.sungam1004.register.global.manager;

import com.sungam1004.register.domain.dto.TokenDto;
import com.sungam1004.register.global.exception.AuthenticationException;
import com.sungam1004.register.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenManager {

    @Value("${token.secret}")
    private String tokenSecret;

    public TokenDto createTokenDto(Long userId) {
        String accessToken = createAccessToken(userId);
        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

    private String createAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject("accessToken")
                .setAudience(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new GregorianCalendar(2023, Calendar.DECEMBER, 31).getTime())
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public Long getUserId(String token) {
        String userId;
        try {
            Claims claims = Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(token).getBody();
            userId = claims.getAudience();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        return Long.valueOf(userId);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("토큰 기한 만료", e);
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {  // 토큰 변조
            log.info("잘못된 jwt token", e);
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.info("jwt token 검증 중 에러 발생", e);
        }
        return false;
    }

}