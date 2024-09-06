package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 생략가능
public interface MemberRepository extends JpaRepository<Member, Long> {
}
