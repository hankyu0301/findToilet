package com.findToilet.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findToilet.domain.member.entity.Member;
import com.findToilet.global.auth.dto.LoginDto;
import com.findToilet.global.auth.jwt.DelegateTokenUtil;
import com.findToilet.global.auth.jwt.JwtTokenizer;
import com.findToilet.global.auth.userdetails.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final DelegateTokenUtil delegateTokenUtil;
    private final JwtTokenizer jwtTokenizer;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws ServletException, IOException {
        MemberDetails memberDetails = (MemberDetails)authResult.getPrincipal();
        Member member = memberDetails.getMember();
        String accessToken = delegateTokenUtil.delegateAccessToken(member);
        String refreshToken = delegateTokenUtil.delegateRefreshToken(member);

        jwtTokenizer.setHeaderAccessToken(response, accessToken);
        jwtTokenizer.setHeaderRefreshToken(response, refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
