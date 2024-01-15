package com.findToilet.domain.member.repository;

import com.findToilet.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByEmail(String email);
}
