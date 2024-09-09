package com.example.springpractice.entity;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@Rollback(value = false)
@Transactional
@SpringBootTest
class MemberTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 30, teamA);
        Member member3 = new Member("member3", 40, teamB);
        Member member4 = new Member("member4", 50, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        List<Member> members
                = entityManager.createQuery("select m from Member m",Member.class)
                .getResultList();

        // 실제 테스트는 검증으로만 이루어져야함 (assert)
        for (Member member : members) {
            log.info("member= {} -> team= {}", member, member.getTeam());
            log.info("member.getTeam().getClass() ={}", member.getTeam().getClass()); // 프록시 객체
        }
        assertThat(members.size()).isEqualTo(4);

    }
}