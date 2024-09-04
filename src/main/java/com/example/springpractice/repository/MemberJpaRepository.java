package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberJpaRepository {

    // @PersistenceContext (영속성 컨텍스트가 엔티티매니저를 가져다줬었음)
    private final EntityManager entityManager;

    public Member save (Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }
}
