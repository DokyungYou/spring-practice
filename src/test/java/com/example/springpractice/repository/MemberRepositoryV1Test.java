package com.example.springpractice.repository;

import com.example.springpractice.connection.ConnectionConst;
import com.example.springpractice.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.example.springpractice.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepositoryV1;

    @BeforeEach
    void beforeEach(){
        // 1. 기본 DriverManager - 항상 새로운 커넥션을 획득
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 2. 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
    }


    // 반복 실행이 불가능한 테스트코드
    // primary key (member_id) 로 돼있고, 아직 db를 비우는 메서드 구현은 안했기때문에
    // 다시 실행하려면 h2 db에서 delete from member 을 먼저 해주자. 
    @Test
    void crud() throws SQLException, InterruptedException {
        // save
        Member member = new Member("회원1",1_000_000);
        memberRepositoryV1.save(member);

        // findById
        Member findMember = memberRepositoryV1.findById(member.getMemberId());
        log.info("findMember={}", findMember);

        assertThat(findMember).isEqualTo(member); // @EqualsAndHashCode

        // update: money(1_000_000 -> 100)
        memberRepositoryV1.update(member.getMemberId(), 100);
        Member updateMember = memberRepositoryV1.findById(member.getMemberId());

        assertThat(updateMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(100);

        // delete
        memberRepositoryV1.delete(member.getMemberId());
        assertThatThrownBy(()-> memberRepositoryV1.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
    }

}