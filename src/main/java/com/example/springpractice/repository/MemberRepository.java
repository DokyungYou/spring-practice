package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository // 생략가능
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
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


    /**
     * 연관된 엔티티들을 SQL 한번에 조회
     * fetch join은 조인 + select 절에 해당 데이터를 다 넣어줌
     *
     *  @EntityGraph
     *  - fetch join의 간편 버전
     *  - LEFT OUTER JOIN 사용
     */
    @Query(value = "select m from Member m left join fetch m.team") // team 이 없는 member도 있기때문에 left join
    List<Member> findMemberFetchJoin();


    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // jpql + 엔티티그래프
    @EntityGraph(attributePaths = {"team"})
    @Query(value = "select m from Member m")
    List<Member> findMemberEntityGraph();

    //@EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all") // NamedEntityGraph
    List<Member> findEntityGraphByUsername(String username);


    /** @QueryHints
     * 해당 기능을 사용하면서 얻는 이점이 생각보다 크지 않음 (애매함)
     * 무작정 적용하지말고 성능테스트 하고, 상황판단해서 결정
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);


    //TODO 비교적 깊은 내용이기때문에 자세한 부분은 책 참고
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
