package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}