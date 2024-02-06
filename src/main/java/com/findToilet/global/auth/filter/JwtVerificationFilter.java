package com.findToilet.global.auth.filter;

import com.findToilet.global.auth.error.AuthenticationError;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.global.auth.utils.AccessTokenRenewalUtil;
import com.findToilet.global.auth.utils.Token;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import com.findToilet.global.util.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final AccessTokenRenewalUtil accessTokenRenewalUtil;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jws = jwtTokenizer.getHeaderAccessToken(request);
            if (redisUtils.hasKeyBlackList(jws))
                throw new CustomException(ExceptionCode.LOGOUT_USER);

            Map<String, Object> claims = jwtTokenizer.verifyJws(jws);
            jwtTokenizer.setAuthenticationToContext(claims);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException eje) {
            try {
                log.error("### 토큰이 만료됐습니다.");
                Token token = accessTokenRenewalUtil.renewAccessToken(request);

                jwtTokenizer.setHeaderAccessToken(response, token.getAccessToken());
                jwtTokenizer.setHeaderRefreshToken(response, token.getRefreshToken());

                Map<String, Object> claims = jwtTokenizer.verifyJws(token.getAccessToken());
                jwtTokenizer.setAuthenticationToContext(claims);
                filterChain.doFilter(request, response);
            } catch (CustomException ce) {
                log.error("### 리프레쉬 토큰을 찾을 수 없음");
                AuthenticationError.sendErrorResponse(response, ce);
            } catch (ExpiredJwtException je) {
                log.error("### 리프레쉬 토큰을 찾을 수 없음");
                jwtTokenizer.resetHeaderRefreshToken(response);
                AuthenticationError.sendErrorResponse(response,
                        new CustomException(ExceptionCode.REFRESH_TOKEN_NOT_FOUND));
            }
        } catch (MalformedJwtException mje) {
            log.error("### 올바르지 않은 토큰 형식입니다.");
            AuthenticationError.sendErrorResponse(response, new CustomException(ExceptionCode.TOKEN_FORMAT_INVALID));
        } catch (SignatureException se) {
            log.error("### 토큰의 서명이 잘못 됐습니다. 변조 데이터일 가능성이 있습니다.");
            AuthenticationError.sendErrorResponse(response, new CustomException(ExceptionCode.SIGNATURE_INVALID));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }
}