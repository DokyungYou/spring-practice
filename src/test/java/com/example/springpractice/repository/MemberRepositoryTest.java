package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberSearchCondition;
import com.example.springpractice.dto.MemberTeamDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.Team;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void basicTest() {
        Member member = new Member("memberA", 15);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();// 바로 get() 은 지양
        assertThat(findMember).isEqualTo(member);

        List<Member> findMembers1 = memberRepository.findAll();
        assertThat(findMembers1).containsExactly(member);

        List<Member> findMembers2 = memberRepository.findByUsername(member.getUsername());
        assertThat(findMembers2).containsExactly(member);
    }

    @Test
    void searchPageSimple() {
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

        Pageable pageable = PageRequest.of(0,3);
        Page<MemberTeamDto> results = memberRepository.searchPageSimple(condition, pageable);

        assertThat(results.getSize()).isEqualTo(3);
        assertThat(results.getNumber()).isEqualTo(0);
        assertThat(results.getContent()).extracting("username")
                .containsExactly("member3","member4");
    }
}