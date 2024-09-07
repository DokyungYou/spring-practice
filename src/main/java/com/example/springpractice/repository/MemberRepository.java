package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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


    /** 카운트 쿼리 분리 - 복잡한 sql에서 사용 (실무에서 매우 중요!!!)
     * ex) 데이터는 left join, 카운트는 left join 안해도 됨
     * 전체 count 쿼리는 비교적 매우 무겁기 때문에, 복잡한 join 과정까지 한다면...
     *
     * 성능테스트를 해본 후 느리다싶으면 이런식으로 고치면 된다.
     *
     * 정렬이 복잡하면 PageRequest에 넣는 정렬조건으로는 해결 X,
     * 조건이 복잡해지면 과감하게 쿼리에 조건을 넣도록 하자
     * */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findPageByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);


    @Modifying(clearAutomatically = true) // 해당 쿼리가 나간 후에 영속성컨텍스트 자동 클리어
    @Query(value = "update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
