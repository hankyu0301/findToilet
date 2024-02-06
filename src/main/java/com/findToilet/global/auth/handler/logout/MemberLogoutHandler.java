package com.findToilet.global.auth.handler.logout;

import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.global.util.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class MemberLogoutHandler implements LogoutHandler {

    private final RedisUtils redisUtils;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String accessToken = jwtTokenizer.getHeaderAccessToken(request);
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            Date expiration = jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey).getBody().getExpiration();
            Long now = new Date().getTime();
            redisUtils.setBlackList(accessToken, "accessToken", expiration.getTime() - now);
        } catch (ExpiredJwtException eje) {
            log.error("### 토큰이 만료되었습니다. 그대로 로그아웃합니다.");
        }

    }
}
