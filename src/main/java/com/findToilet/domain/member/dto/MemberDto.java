package com.findToilet.domain.member.dto;

import com.findToilet.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private Member.Role role;

    public static MemberDto of(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getRole());
    }
}
