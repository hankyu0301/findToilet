package com.findToilet.domain.oauth.service;

import java.util.Map;
import java.util.Optional;

import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.oauth.entity.KakaoToken;
import com.findToilet.domain.oauth.repository.KakaoTokenRepository;
import com.findToilet.domain.oauth.template.KakaoTemplate;
import com.findToilet.domain.oauth.template.KakaoTemplateConstructor;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoApiService {
    private final KakaoTokenRepository kakaoTokenRepository;

    private final KakaoTemplateConstructor kakaoTemplateConstructor;

    private final KakaoTokenOauthService kakaoTokenOauthService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String apiKey;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String apiSecret;

    private final String messageApiUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final String tokenRenewalApiUri = "https://kauth.kakao.com/oauth/token";
    private final String unlinkKakaoApiUri = "https://kapi.kakao.com/v1/user/unlink";

    private final Gson gson;

    @Async
    public void sendMessage(Object template, String accessToken) {
        String body = gson.toJson(template);

        String result = WebClient.create(messageApiUrl)
            .post()
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + accessToken)
            .body(BodyInserters.fromFormData("template_object", body))
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, // 4xx 에러인 경우
                clientResponse -> {
                    renewKakaoAccessTokenAndResend(template, accessToken);
                    return Mono.empty();
                })
            .bodyToMono(String.class)
            .block(); // 메시지 전송
    }

    private void renewKakaoAccessTokenAndResend(Object template, String accessToken) {
        KakaoToken kakaoToken = kakaoTokenRepository.findByAccessToken(accessToken)
            .orElseThrow();

        WebClient.create(tokenRenewalApiUri)
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                .with("client_id", apiKey)
                .with("client_secret", apiSecret)
                .with("refresh_token", kakaoToken.getRefreshToken()))
            .retrieve()
            .bodyToMono(Map.class)
            .doOnNext(tokenResponse -> {
                log.info(tokenResponse.toString());
                String refreshToken = (String)Optional.ofNullable(tokenResponse.get("refresh_token"))
                    .orElseGet(kakaoToken::getRefreshToken);
                kakaoTokenOauthService.saveOrUpdateToken(tokenResponse.get("access_token").toString(),
                    refreshToken, kakaoToken.getMember());
            })
            /*.flatMap(tokenResponse -> {
                // 토큰 갱신 후 다시 메시지 보내기
                *//*String renewedAccessToken = tokenResponse.get("access_token").toString();
                sendMessage(template, renewedAccessToken);*//*
                return Mono.empty();
            })*/
            .block();
    }

    @Async
    public void sendWelcomeMessage(Member member, String accessToken) {
        KakaoTemplate.Feed feedTemplate = kakaoTemplateConstructor.getWelcomeTemplate(member);

        sendMessage(feedTemplate, accessToken);
    }

    public void unlinkKaKaoService(String accessToken) {
        WebClient.create(unlinkKakaoApiUri)
            .post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
