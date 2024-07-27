package com.example.springpractice.member.service;

import com.example.springpractice.member.Member;

public interface MemberService {

    void signup(Member member);

    Member findMember(Long id);
}
