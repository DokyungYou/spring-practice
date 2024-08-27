package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;


    //@Rollback(false) // insert문, 실제 db 등 확인하고싶으면 주석해제
    @DisplayName("회원가입")
    @Test
    void join_success() {

        Member member = new Member();
        member.setName("Y");

        Long savedId = memberService.join(member);

        /*  Member에 EqualsHashCode 재정의하지 않았음에도 같은 객체라고 나오는 이유
            JPA에서 같은 트랜잭션 안에서 id(pk)값이 동일하면 같은 영속성 컨텍스트에서 같은 대상으로 하나로 관리됨 */

        //entityManager.flush(); // db에 쿼리를 날리게 함, 영속성 컨텍스트에 있는 어떠한 변경이나 등록 내용을 db에 반영
        assertEquals(member, memberRepository.findOne(savedId));
    }

//    @DisplayName("중복회원 예외")
//    @Test
//    void join_fail(){
//
//        // given
//        Member member1 = new Member();
//        member1.setName("YY");
//        Member member2 = new Member();
//        member2.setName("YY");
//
//        // when
//        memberService.join(member1);
//
//        try{
//            memberService.join(member2);
//        }catch (IllegalStateException e){
//            return;
//        }
//
//        // then
//        fail("예외가 발생해야 한다.");
//    }

    @DisplayName("중복회원 예외")
    @Test
    void join_fail(){

        // given
        Member member1 = new Member();
        member1.setName("YY");
        Member member2 = new Member();
        member2.setName("YY");

        // when
        memberService.join(member1);

        assertThatThrownBy(()-> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }
}