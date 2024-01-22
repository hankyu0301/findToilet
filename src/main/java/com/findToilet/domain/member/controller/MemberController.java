package com.findToilet.domain.member.controller;

import com.findToilet.domain.member.dto.MemberCreateRequest;
import com.findToilet.domain.member.dto.MemberDto;
import com.findToilet.domain.member.dto.MemberUpdateRequest;
import com.findToilet.domain.member.service.MemberService;
import com.findToilet.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<Object> register(@Valid @RequestBody MemberCreateRequest req) {
        memberService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<ResponseDto<MemberDto>> findMember(@PathVariable Long id) {
        MemberDto memberDto = memberService.readMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(memberDto));
    }

    @PutMapping("/api/members/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody MemberUpdateRequest req) {
        memberService.updateMember(id, req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/members/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
