package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findById(Long id); // Optional은 Java8에서 추가되었음
    Optional<Member> findByName(String name); // Optional은 Java8에서 추가되었음
    List<Member> findAll();
}
