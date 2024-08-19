package com.example.springpractice.service;

import com.example.springpractice.connection.ConnectionConst;
import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.springpractice.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

/**
 * 기본 동작 O,
 * 트랜잭션이 없어서 문제발생
 */
class MemberServiceV1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberServiceV1 memberServiceV1;
    private MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void before() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepositoryV1);
    }
    @AfterEach
    void after() throws SQLException {
        memberRepositoryV1.deleteAll();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 20000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);

        //when (method)
        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 5000);

        //then (assert)
        Member findMemberA = memberRepositoryV1.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV1.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(5000);
        assertThat(findMemberB.getMoney()).isEqualTo(25000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 20000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberEx);

        //when (method)
        assertThatThrownBy(()-> memberServiceV1.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 5000))
                .isInstanceOf(IllegalArgumentException.class);

        //then (assert)
        Member findMemberA = memberRepositoryV1.findById(memberA.getMemberId());
        Member findMemberEx = memberRepositoryV1.findById(memberEx.getMemberId());

        // 트랜잭션X, 자동커밋이기때문에 fromMember 의 금액만 차감됨
        assertThat(findMemberA.getMoney()).isEqualTo(5000);
        assertThat(findMemberEx.getMoney()).isEqualTo(20000);
    }
}