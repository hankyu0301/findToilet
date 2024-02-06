package com.findToilet.global.auth.handler.login;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.global.auth.userdetails.MemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails)authentication.getPrincipal();
        log.info("### Authenticated successfully!");
        log.info("### 로그인 정보 이메일: " + memberDetails.getMember().getEmail());
    }
}
