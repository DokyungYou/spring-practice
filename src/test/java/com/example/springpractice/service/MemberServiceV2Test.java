package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepositoryV1;
import com.example.springpractice.repository.MemberRepositoryV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.springpractice.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 트랜잭션 - 커넥션 파라미터 전달 방식 동기화
 */
class MemberServiceV2Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberServiceV2 memberServiceV2;
    private MemberRepositoryV2 memberRepositoryV2;

    @BeforeEach
    void before() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV2 = new MemberRepositoryV2(dataSource);
        memberServiceV2 = new MemberServiceV2(memberRepositoryV2, dataSource);
    }
    @AfterEach
    void after() throws SQLException {
        memberRepositoryV2.deleteAll();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 20000);
        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(memberB);

        //when (method)
        memberServiceV2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 5000);

        //then (assert)
        Member findMemberA = memberRepositoryV2.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV2.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(5000);
        assertThat(findMemberB.getMoney()).isEqualTo(25000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given (parameter)
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 20000);
        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(memberEx);

        //when (method)
        assertThatThrownBy(()-> memberServiceV2.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 5000))
                .isInstanceOf(IllegalArgumentException.class);

        //then (assert)
        Member findMemberA = memberRepositoryV2.findById(memberA.getMemberId());
        Member findMemberEx = memberRepositoryV2.findById(memberEx.getMemberId());

        // 트랜잭션O, 롤백
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberEx.getMoney()).isEqualTo(20000);
    }
}