package com.example.springpractice;

import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.example.springpractice.entity.QTeam;
import com.example.springpractice.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.springpractice.entity.QMember.member;
import static com.example.springpractice.entity.QTeam.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Rollback(value = false)
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

    @Test
    void aggregation() {
        //com.querydsl.core.Tuple
        List<Tuple> result = queryFactory // 실무에선 Tuple 보다 DTO로 직접 받는 방법
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                ).from(member)
                .fetch(); // 이 경우는 fetchOne 을 해도 됨

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(140);
        assertThat(tuple.get(member.age.avg())).isEqualTo(35);
        assertThat(tuple.get(member.age.max())).isEqualTo(50);
        assertThat(tuple.get(member.age.min())).isEqualTo(20);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령 구하기
     */
    @Test
    void group() {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team) // 두번째 인자는 alias
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(25);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(45);
    }


    /**
     * 팀 A에 소속된 모든 회원 조회
     */
    @Test
    void join() {
        List<Member> result = queryFactory.selectFrom(member)
                .join(member.team, team) // inner join
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result.size()).isEqualTo(2);

        assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");
    }


    /**
     * 세타 조인(연관관계가 없는 필드로 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    void theta_join() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        entityManager.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team) // 모든 Member, Team 테이블을 조인
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result).extracting("username")
                .containsExactly("teamA","teamB");
    }

    /** on절로 조인대상 필터링
     *
     * ex) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t from Member m left join Team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and t.name='teamA
     * MEMBER_ID  	AGE  	TEAM_ID  	USERNAME  	TEAM_ID  	NAME
     *     1	    20	        1	     member1	    1	    teamA
     *     2	    30	        1	     member2	    1	    teamA
     *     3	    40	        2	     member3	   null	    null
     *     4	    50	        2	     member4	   null	    null
     *
     *
     *  ex) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 팀은 모두 조회
     * JPQL: select m, t from Member m right join Team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m RIGHT JOIN Team t ON m.TEAM_ID=t.id and t.name='teamA
     * MEMBER_ID  	AGE  	TEAM_ID  	USERNAME  	TEAM_ID  	NAME
     *      1	     20	        1	     member1	    1	    teamA
     *      2	     30	        1	     member2	    1	    teamA
     *     null	     null	   null	      null	        2	    teamB
     */

    @Test
    void join_on_filtering() {
        List<Tuple> result1 = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result1) {
            log.info("leftJoin tuple ={}", tuple);
        }


        List<Tuple> result2 = queryFactory
                .select(member, team)
                .from(member)
                .rightJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result2) {
            log.info("rightJoin tuple ={}", tuple);
        }


        // 이너 조인이면 on 절, where절 결과 동일
        List<Tuple> result3 = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                //.on(team.name.eq("teamA"))
                .where(team.name.eq("teamA")) // 결과 동일
                .fetch();

        for (Tuple tuple : result3) {
            log.info("innerJoin tuple ={}", tuple);
        }
    }


    /**
     * 연관관계 없는 엔티티 외부 조인
     * ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     * JPQL: SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name  
     * * SQL: SELECT m.*, t.* FROM  Member m LEFT JOIN Team t ON m.username = t.name 
     * */
    @Test
    void join_on_no_relation() {
        entityManager.persist(new Member("teamA"));
        entityManager.persist(new Member("teamB"));
        entityManager.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member,team)
                .from(member)
                .leftJoin(team) // member.team X ,   member.team -> sql의 on 절에 m.team_id = t.team_id
                .on(member.username.eq(team.name))
                .fetch();

        assertThat(result).extracting("username")
                .containsExactly("teamA","teamB");
    }
}
