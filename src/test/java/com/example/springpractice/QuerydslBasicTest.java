package com.example.springpractice;

import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.example.springpractice.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.springpractice.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class QuerydslBasicTest {

    @Autowired
    EntityManager entityManager;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(entityManager);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 30, teamA);
        Member member3 = new Member("member3", 40, teamB);
        Member member4 = new Member("member4", 50, teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
    }

    @Test
    void startJPQL() {
        // member1 찾기
        Member findMember = entityManager
                .createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    void startQuerydsl() {

        QMember m = new QMember("m"); // alias (보통 평소에는 사용X, 같은 테이블을 조인해야하는 경우에 사용)
        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1")) // 자동으로 jdbc에 있는 prepate statement로 파라미터 바인딩
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl2() {
        QMember m = member;
        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1")) // 자동으로 jdbc에 있는 prepate statement로 파라미터 바인딩
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    @Test
    void startQuerydsl3() {
        // QMember.member -> static import
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")) // 자동으로 jdbc에 있는 prepate statement로 파라미터 바인딩
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(20)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)

                // where(Predicate... o) 쉼표로 여러개를 넘기면, 모두 and
                // 이 경우 중간에 null 이 들어가면 무시 (동적쿼리 만들 때 아주 좋은 조건)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(20))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    void resultFetchTest() {

        // List
        List<Member> members = queryFactory
                .selectFrom(member)
                .fetch();

        // 단건 조회
//       Member findMember1 = queryFactory
//                .selectFrom(member)
//                .fetchOne();

       // 처음 한 건 조회
        Member findMember2 = queryFactory
                .selectFrom(member)
                .fetchFirst(); // .limit(1).fetchOne()

        // 페이징
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults(); // deprecated

        long totalCount = queryFactory
                .selectFrom(member)
                .fetchCount();// deprecated
    }


    /**
     * 회원 정렬순서
     * 1. 회원 나이 내림차순
     * 2. 회원 이름 올림차순
     * 3. 2에서 회원 이름이 없으면 마지막에 출력 (nullsLast)
     */
    @Test
    void sort() {
        entityManager.persist(new Member(null, 50));
        entityManager.persist(new Member(null, 100));
        entityManager.persist(new Member("member5", 100));
        entityManager.persist(new Member("member6", 30));

        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(
                        member.age.desc(),
                        member.username.asc().nullsLast()) // nullsFirst()
                .fetch();

        // 실제로 테스트시엔 출력 X, 검증으로만 이루어져야함
        int order = 0;
        for (Member member : members) {
            log.info("{}st member = {}", ++order , member);
        }
    }

    @Test
    void paging1() {
        Pageable pageable = PageRequest.of(1,2);

        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                .select(member.count())
                .from(member);

        // fetchOne 메서드를 인수로 전달하고, 조건에 따라 실행여부 결정
        Page<Member> page = PageableExecutionUtils.getPage(result, pageable, totalCount::fetchOne);

    }
}
