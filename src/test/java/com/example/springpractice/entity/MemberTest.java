package com.example.springpractice.entity;

import com.example.springpractice.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@Rollback(value = false)
@SpringBootTest
class MemberTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 연관관계 테스트
     */
    @Test
    void testEntity(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("회원1", 50, teamA);
        Member member2 = new Member("회원2", 25, teamA);
        Member member3 = new Member("회원3", 29, teamB);
        Member member4 = new Member("회원4", 15, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        // 실제 나가는 쿼리문 보기 위해 세팅
        entityManager.flush(); // 강제로 실제 db에 반영
        entityManager.clear(); // 영속성 컨텍스트에 있는 캐시를 비움

        List<Member> members = entityManager
                .createQuery("select m from Member m", Member.class)
                .getResultList();
        
        for (Member member : members) {
            // 찾기 힘들어서 @넣음
            log.info("@ member= {} -> team= {}", member, member.getTeam());
        }
        
        // 검증과정 생략
    }

    @Test
    void jpaEventBaseEntity() throws Exception {
        Member member = new Member("회원1");
        memberRepository.save(member); // @PrePersist 작동

        Thread.sleep(100); // createdAt , updateAt 시간 구분해서 보기 위함 (실제로는 이런 방식은 지양)
        member.setUsername("회원2"); // @PreUpdate 작동

        entityManager.flush();
        entityManager.clear();

        Member findMember = memberRepository.findById(member.getId()).get();// 바로 get 지양

        log.info("findMember createdAt= {}", findMember.getCreatedAt());
        log.info("findMember updatedAt= {}", findMember.getLastModifiedAt());
        log.info("findMember createdBy= {}", findMember.getCreatedBy());
        log.info("findMember lastModifiedBy= {}", findMember.getLastModifiedBy());
    }

}