package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.MemberRepositoryV4_1;
import com.example.springpractice.repository.MemberRepositoryV4_2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private MemberServiceV4 memberService;

    @Autowired
    private MemberRepository memberRepository;


    @TestConfiguration
    @RequiredArgsConstructor
    static class testConfig{

        // 스프링이 자동으로 만든 데이터소스를 넣어줌
        private final DataSource dataSource;

        @Bean
        MemberRepository memberRepository(){
            //return new MemberRepositoryV4_1(dataSource);
            return new MemberRepositoryV4_2(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV4(){
            return new MemberServiceV4(memberRepository());
        }

    }

    @AfterEach
    void after() {
        memberRepository.deleteAll();
    }

    @Test
    void AopCheck(){ // 프록시가 적용된 것을 볼 수 있음

        // memberService class=class com.example.springpractice.service.MemberServiceV3_3$$SpringCGLIB$$0
        log.info("memberService class={}", memberService.getClass());

        // memberRepository class=class com.example.springpractice.repository.MemberRepositoryV3
        log.info("memberRepository class={}", memberRepository.getClass());

        assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 20000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when (method)
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 5000);

        //then (assert)
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(5000);
        assertThat(findMemberB.getMoney()).isEqualTo(25000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 20000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when (method)
        assertThatThrownBy(()-> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 5000))
                .isInstanceOf(IllegalArgumentException.class);

        //then (assert)
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberEx = memberRepository.findById(memberEx.getMemberId());

        // 트랜잭션O, 롤백
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberEx.getMoney()).isEqualTo(20000);
    }

}