package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
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
}