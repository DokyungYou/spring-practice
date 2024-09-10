package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // select m from Member m where m.username = :username;
    List<Member> findByUsername(String username);


}
