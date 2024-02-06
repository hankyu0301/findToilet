package com.findToilet.global.auth.utils;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.global.auth.jwt.DelegateTokenUtil;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class AccessTokenRenewalUtil {
    private final MemberRepository memberRepository;
    private final DelegateTokenUtil delegateTokenUtil;
    private final JwtTokenizer jwtTokenizer;

    public Token renewAccessToken(HttpServletRequest request) {
        try {
            String refreshToken = jwtTokenizer.getHeaderRefreshToken(request);
            String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
            String email = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody().getSubject();
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
            String newAccessToken = delegateTokenUtil.delegateAccessToken(member);
            return Token.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (CustomException | ExpiredJwtException ce) {
            throw ce;
        }
    }


}