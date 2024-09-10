package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.springpractice.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberJpaRepository { 
//   JPA 과 Querydsl 비교

    // TODO 엔티티매니저의 동시성 문제에 대한 부분 공부 (13.1 트랜잭션 범위의 영속성 컨텍스트)
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory; // 따로 빈으로 등록해놨음

//    public MemberJpaRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//        this.queryFactory = new JPAQueryFactory(entityManager);
//    }


    public void save(Member member){
        entityManager.persist(member);
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public List<Member> findAll(){
        return entityManager
                .createQuery("select m from Member m", Member.class)
                .getResultList();
    }


    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername(String username){
        return entityManager
                .createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsername_Querydsl(String username){
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }



}
