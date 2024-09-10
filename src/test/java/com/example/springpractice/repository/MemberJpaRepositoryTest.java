package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberSearchCondition;
import com.example.springpractice.dto.MemberTeamDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberJpaRepository memberJpaRepository;


    @Test
    void basicTest() {
        Member member = new Member("memberA", 15);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();// 바로 get() 은 지양
        assertThat(findMember).isEqualTo(member);

        List<Member> findMembers1 = memberJpaRepository.findAll();
        assertThat(findMembers1).containsExactly(member);

        List<Member> findMembers2 = memberJpaRepository.findByUsername(member.getUsername());
        assertThat(findMembers2).containsExactly(member);
    }

    @Test
    void basicTest_Querydsl() {
        Member member = new Member("memberA", 15);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();// 바로 get() 은 지양
        assertThat(findMember).isEqualTo(member);

        List<Member> findMembers1 = memberJpaRepository.findAll_Querydsl();
        assertThat(findMembers1).containsExactly(member);

        List<Member> findMembers2 = memberJpaRepository.findByUsername_Querydsl(member.getUsername());
        assertThat(findMembers2).containsExactly(member);
    }

    @Test
    void searchTest() {
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

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(30);
        condition.setAgeLoe(50);
        condition.setTeamName("teamB");

        List<MemberTeamDto> results = memberJpaRepository.searchByBuilder(condition);

        assertThat(results.size()).isEqualTo(2);
        assertThat(results).extracting("username")
                .containsExactly("member3","member4");

    }
}