package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // 각각의 테스트 완료 후에 항상 롤백
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;



    //@Commit
    @DisplayName("회원가입 성공")
    @Test
    void join() {
        //given (parameter)
        Member member = new Member();
        member.setName("YouYou");

        //when (method)
        Long savedId = memberService.join(member);

        //then (assert)
        Member result = memberService.findMember(savedId).get();
        assertThat(result.getName()).isEqualTo(member.getName());

    }

    @DisplayName("회원가입 실패: 중복회원")
    @Test
    void joinDuplicate() {
        //given (parameter)
        Member member1 = new Member();
        member1.setName("YouYou");

        Member member2 = new Member();
        member2.setName("YouYou");

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