package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    public void basicCRUD(){
        Member member1 = new Member("멤버1");
        Member member2 = new Member("멤버2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();// 바로 get() 지양
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();// 바로 get() 지양
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        assertThat(memberJpaRepository.count()).isEqualTo(0);

    }

    @Test
    public void findByUserNameAgeGreaterThen() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("멤버", 20);
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getAge()).isEqualTo(30);
        assertThat(members.get(0).getUsername()).isEqualTo("멤버");
    }

    @Test
    void testNamedQuery() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsername("멤버");
        assertThat(members.size()).isEqualTo(2);
    }


    @Test
    void paging() {

        for (int i = 0; i < 5; i++) {
            memberJpaRepository.save(new Member("중학생",15));
        }
        int age = 15;
        int offset = 0;
        int limit = 3;

        long totalCount = memberJpaRepository.totalCount(age, offset, limit);
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);

        assertThat(totalCount).isEqualTo(5);
        assertThat(members.size()).isEqualTo(3);
    }
}