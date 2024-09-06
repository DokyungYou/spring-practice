package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository // 생략가능
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * By로 끝내면 전체조회
     * find ... By (...에 식별하기 위한 내용이 들어가기도 함)
     */
    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
    List<Member> findNamedByUsername(@Param("username") String username);


    // JPA Named 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있음
    @Query(value = "select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUserByUsernameAndAge(@Param("username") String username, @Param("age") int age);

    @Query(value = "select m.username from Member m")
    List<String> findUsernameList();

    @Query(value = "select new com.example.springpractice.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDtoList();

    @Query(value = "select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findMemberOptionalByUsername(String username); // Optional 단건
}
