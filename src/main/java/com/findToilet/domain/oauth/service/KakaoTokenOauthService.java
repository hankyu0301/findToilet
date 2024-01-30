package com.findToilet.domain.oauth.service;

import java.util.Optional;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.oauth.entity.KakaoToken;
import com.findToilet.domain.oauth.repository.KakaoTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class KakaoTokenOauthService {

    private final KakaoTokenRepository kakaoTokenRepository;

    public void saveOrUpdateToken(String accessToken, String refreshToken, Member member) {
        Optional<KakaoToken> findToken = kakaoTokenRepository.findByMember(member);
        if (findToken.isEmpty()) {
            KakaoToken token = KakaoToken.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
            kakaoTokenRepository.save(token);
        } else {
            findToken.get().updateToken(accessToken, refreshToken);
        }
    }
}
