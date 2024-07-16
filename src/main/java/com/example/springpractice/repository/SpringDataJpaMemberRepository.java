package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 스프링 데이터 JPA 가 JPA 레파지토리를 받고 있으면 구현체를 자동으로 만들어주고, 스프링 빈에 자동으로 등록해준다.
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository { // 인터페이스가 인터페이스를 받을땐 extends


    // MemberRepository 에 있는 메서드들을 구현해야한다.
    // JpaRepository 가 제공해주는 메서드는 구현할 필요가 없다. (그 외의 것은 직접 구현)
    @Override
    Optional<Member> findByName(String name);  // jpql로 번역돼서 실행이 된다. select m from Member m where m.name = ?

    @Override
    default void clear(){};
}
