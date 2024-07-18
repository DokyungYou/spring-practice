package com.example.springpractice.member.service;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl(new MemoryMemberRepository());


    @BeforeEach
    void init(){

    }

    @DisplayName("회원가입 - 성공")
    @Test
    void signup(){
        //given (parameter)
        Member member = new Member(1L, "회원1", Grade.BASIC);

        //when (method)
        memberService.signup(member);
        Member findedMember = memberService.findMember(member.getId());

        //then (assert)
        assertThat(member).isEqualTo(findedMember);
    }

}