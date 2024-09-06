package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberJpaRepository {

    // @PersistenceContext (영속성 컨텍스트가 엔티티매니저를 가져다줬었음)
    private final EntityManager entityManager;

    public Member save (Member member) {
        entityManager.persist(member);
        return member;
    }

    public void delete(Member member){
        entityManager.remove(member);
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public List<Member> findAll(){
        return entityManager
                .createQuery("select m from Member m ", Member.class)
                .getResultList();
    }

    public long count(){
        return entityManager
                .createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return entityManager
                .createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
            return entityManager
                .createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username",username)
                .getResultList();
    }
}
