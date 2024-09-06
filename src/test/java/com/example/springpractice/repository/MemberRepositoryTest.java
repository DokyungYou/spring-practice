package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void testMember() {

        log.info("memberRepository = {}", memberRepository.getClass()); // class jdk.proxy3.$Proxy131
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // 바로 꺼내는 방식 지양
        
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // JPA 엔티티 동일성 보장 (같은 트랜잭션 내에서 영속성 컨텍스트의 동일성 보장)
        }


    @Test
    public void basicCRUD(){
        Member member1 = new Member("멤버1");
        Member member2 = new Member("멤버2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();// 바로 get() 지양
        Member findMember2 = memberRepository.findById(member2.getId()).get();// 바로 get() 지양
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        assertThat(memberRepository.count()).isEqualTo(0);

    }

    @Test
    public void findByUserNameAgeGreaterThen() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("멤버", 20);
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getAge()).isEqualTo(30);
        assertThat(members.get(0).getUsername()).isEqualTo("멤버");
    }

    @Test
    public void findHelloBy() {
        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findHelloBy();
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void findTop3HelloBy() {
        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        Member member3 = new Member("멤버", 30);
        Member member4 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> members = memberRepository.findTop3HelloBy();
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    void testNamedQuery() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findNamedByUsername("멤버");
        assertThat(members.size()).isEqualTo(2);
    }
}