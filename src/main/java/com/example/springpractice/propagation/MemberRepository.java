package com.example.springpractice.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager entityManager;


    @Transactional
    public void save(Member member) {
        log.info("member 저장");
        entityManager.persist(member);
    }
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public Optional<Member> findByUsername(String username) {
       return entityManager.createQuery("select m from Member m where m.username = :username", Member.class)
               .setParameter("username", username)
               .getResultList()
               .stream()
               .findAny();

        // getSingleResult() 로 가능은 하지만 값이 없으면 예외가 발생하기때문에 .getResultList()로 받음
    }
}