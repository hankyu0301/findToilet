package com.findToilet.domain.member.service;

import com.findToilet.domain.member.dto.MemberCreateRequest;
import com.findToilet.domain.member.dto.MemberDto;
import com.findToilet.domain.member.dto.MemberUpdateRequest;
import com.findToilet.domain.member.entity.Member;
import com.findToilet.domain.member.repository.MemberRepository;
import com.findToilet.global.exception.CustomException;
import com.findToilet.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberCreateRequest req) {
        validateMemberCreateRequest(req);
        memberRepository.save(createMember(req));
    }

    private Member createMember(MemberCreateRequest req) {
        return Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .build();
    }

    private void validateMemberCreateRequest(MemberCreateRequest req) {
        if(memberRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(ExceptionCode.EMAIL_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public MemberDto readMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
        return MemberDto.of(member);
    }

    public void updateMember(Long id, MemberUpdateRequest req) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
        member.setNickname(req.getNickname());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
