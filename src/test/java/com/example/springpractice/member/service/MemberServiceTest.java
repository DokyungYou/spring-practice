package com.example.springpractice.member.service;

import com.example.springpractice.AppConfig;
import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest {

    MemberService memberService;


    @BeforeEach
    void init(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
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