
package com.findToilet.global.auth.userdetails;
import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.domain.oauth.service.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final KakaoApiService kakaoApiService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        String accessToken = userRequest.getAccessToken().getTokenValue();

        validateOAuth2User(attributes, accessToken);

        return attributes;
    }

    private void validateOAuth2User(OAuthAttributes attributes, String accessToken) {
        Optional<Member> optionalMember = memberRepository.findByEmail(attributes.getEmail());
        if (optionalMember.isEmpty()) {
            Member member = oauthAttributesToMember(attributes);
            memberRepository.save(member);
            kakaoApiService.sendWelcomeMessage(member, accessToken);
        }
    }

    private Member oauthAttributesToMember(OAuthAttributes oAuthAttributes) {
        if (oAuthAttributes == null) {
            return null;
        }
        return Member.builder()
                .email(oAuthAttributes.getEmail())
                .nickname(oAuthAttributes.getNickname())
                .build();
    }
}
