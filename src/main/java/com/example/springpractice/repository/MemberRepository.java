package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략가능
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * By로 끝내면 전체조회
     * find ... By (...에 식별하기 위한 내용이 들어가기도 함)
     */
    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();
}
