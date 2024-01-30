package com.findToilet.domain.oauth.template;

import com.findToilet.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class KakaoTemplateConstructor {

    public KakaoTemplate.Feed getWelcomeTemplate(Member member) {
        String nickname = member.getNickname();

        KakaoTemplate.Content content = KakaoTemplate.Content.builder()
                .title(String.format("%s님의 가입을 환영합니다!", nickname))
                .description("findToilet을 이용하러 가볼까요?")
                .build();

        KakaoTemplate.Feed feed = KakaoTemplate.Feed.builder()
                .content(content)
                .button_title("findToilet으로 이동")
                .build();

        return feed;
    }


}
