package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

//    @PersistenceContext
//    private EntityManager entityManager;

    // 본래 EntityManager 는  @PersistenceContext 라는 표준 애노테이션이 있어야 주입이 가능한데,
    // spring data JPA 가  자동주입도 가능하게 지원을 해주기때문에 생성자주입 가능
    private final EntityManager entityManager;

    public void save(Member member){

        // 영속성 컨텍스트에 member 엔티티를 올린 후 트랜잭션이 commit 되는 시점에 db에 반영
        entityManager.persist(member);
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
