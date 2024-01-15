package com.findToilet.domain.member.entity;

import com.findToilet.domain.oauth.entity.KakaoToken;
import com.findToilet.global.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 32, columnDefinition = "varchar(32) default 'ROLE_USER'")
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private KakaoToken kakaoToken;

    @Getter
    public enum Role {
        ADMIN("ROLE_ADMIN", "ROLE_USER"),
        USER("ROLE_USER"),
        SOCIAL("ROLE_USER");
        // SOCIAL("ROLE_USER", "ROLE_SOCIAL");

        private final String[] roles;

        Role(String... roles) {
            this.roles = roles;
        }
    }

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = Role.USER;
    }

    @Builder
    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.role = Role.SOCIAL;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
