package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.MemoryMemberRepository;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach(){ // DI
        memberRepository = new MemoryMemberRepository();
        memberService  = new MemberService(memberRepository);
    }

    @AfterEach
    void clear(){
        memberRepository.clear();
        // 현재 MemoryMemberRepository의 store가 static이어서 매번 clear() 필요
    }

    @DisplayName("회원가입 성공")
    @Test
    void join() {
        //given (parameter)
        Member member = new Member();
        member.setName("Kim");

        //when (method)
        Long savedId = memberService.join(member);

        //then (assert)
        Member result = memberService.findMember(savedId).get();
        assertThat(result).isEqualTo(member);
        assertThat(result.getName()).isEqualTo(member.getName());

    }

    @DisplayName("회원가입 실패: 중복회원")
    @Test
    void joinDuplicate() {
        //given (parameter)
        Member member1 = new Member();
        member1.setName("Kim");

        Member member2 = new Member();
        member2.setName("Kim");

        //when (method)
        memberService.join(member1);

//        try{
//            memberService.join(member2);
//            fail("예외가 발생해야 합니다.");
//        }catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }

        //then (assert)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}