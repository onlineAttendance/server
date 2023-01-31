package com.sungam1004.register.global.config;

import com.sungam1004.register.global.exception.AuthenticationException;
import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.manager.TokenManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰 유무 확인
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new AuthenticationException(ErrorCode.EMPTY_AUTHORIZATION);
        }

        // GrantType 이 Bearer 인지 검증
        String[] authorizations = authorizationHeader.split(" ");
        if (authorizations.length < 2 || (!"Bearer".equals(authorizations[0].toUpperCase()))) {
            throw new AuthenticationException(ErrorCode.NOT_BEARER_GRANT_TYPE);
        }

        // 토큰 유효성 검증
        String accessToken = authorizations[1];
        if (!tokenManager.validateToken(accessToken)) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }

        return true;
    }
}