package com.example.springpractice;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // 스프링부트여서 스프링 컨테이너 위에서 모든게 다 동작하는데, 해당 애노테이션이 있으면 엔티티매니저를 주입해줌
    @PersistenceContext
    private EntityManager entityManager;


    // 리턴값에 대한 설명은 노트참고 (커맨드와 쿼리 분리관련)
    public Long save(Member member){
        entityManager.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return entityManager.find(Member.class, id);
    }
}
