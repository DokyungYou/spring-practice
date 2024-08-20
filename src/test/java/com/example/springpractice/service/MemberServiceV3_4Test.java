package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepositoryV3;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - DataSource, transactionManager 자동 등록
 */
@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private MemberServiceV3_3 memberService;

    @Autowired
    private MemberRepositoryV3 memberRepository;


    @TestConfiguration
    @RequiredArgsConstructor
    static class testConfig{
        
        // 스프링이 자동으로 만든 데이터소스를 넣어줌
        private final DataSource dataSource;


        @Bean
        MemberRepositoryV3 memberRepositoryV3(){
            return new MemberRepositoryV3(dataSource);
        }

        @Bean
        MemberServiceV3_3 memberServiceV3_3(){
            return new MemberServiceV3_3(memberRepositoryV3());
        }

    }
//    @BeforeEach
//    void before() throws SQLException {
//        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//        memberRepository = new MemberRepositoryV3(dataSource);
//
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
//        memberService = new MemberServiceV3_3(memberRepository);
//    }

    @AfterEach
    void after() throws SQLException {
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
    void accountTransfer() throws SQLException {
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
    void accountTransferEx() throws SQLException {
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