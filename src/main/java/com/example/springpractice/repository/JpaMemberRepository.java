package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    // JPA를 쓰려면 EntityManager를 주입받아야한다
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) { //스프링 부트가 자동으로 EntityManager 라는 것을 생성해주기때문에 주입만 받으면 된다.
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // pk기반이 아닌 값으로 찾을 때는 jpql를 작성해줘야한다
    @Override
    public Optional<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList().stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
        // 테이블이 아닌 객체(Entity)를 대상으로 쿼리를 날린다 -> sql로 번역
        // 엔티티 자체를 select 한다
    }

    @Override
    public void clear() {

    }
}
