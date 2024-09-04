package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // JPA 의 모든 데이터 변경은 트랜잭션 안에서 이뤄져야 한다.
@Rollback(false) // 테스트의 트랜잭션은 롤백되므로, 실습을 위해 false 처리
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // JPA 엔티티 동일성 보장 (같은 트랜잭션 내에서 영속성 컨텍스트의 동일성 보장)
    }
}