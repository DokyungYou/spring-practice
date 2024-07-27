package com.example.springpractice.member.repository;

import com.example.springpractice.member.Member;

public interface MemberRepository {
    void save(Member member);
    Member findById(Long id);
}
