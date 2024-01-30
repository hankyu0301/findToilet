package com.findToilet.domain.oauth.repository;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.oauth.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByAccessToken(String accessToken);

    Optional<KakaoToken> findByMember(Member member);
}
