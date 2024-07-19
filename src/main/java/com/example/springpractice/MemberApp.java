package com.example.springpractice;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;

public class MemberApp {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();

        Member member1 = new Member(1L, "회원1", Grade.BASIC);
        memberService.signup(member1);

        Member findedMember = memberService.findMember(1L);
        System.out.println(member1);
        System.out.println(findedMember);

        System.out.println(member1.equals(findedMember));
    }
}
