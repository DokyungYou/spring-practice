package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Member member){
        entityManager.persist(member); // 영속성 컨텍스트에 member 엔티티를 넣은 후 트랜잭션이 commit 되는 시점에 db에 반영
    }
    public Member findOne(Long id){
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll(){
        return entityManager.createQuery("select m from Member m", Member.class) // sql은 테이블, jpql은 Entity 객체를 대상으로 쿼리
                .getResultList();
    }

    public List<Member> findByName(String name){
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
