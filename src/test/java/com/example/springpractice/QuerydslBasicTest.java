package com.example.springpractice;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.dto.QMemberDto;
import com.example.springpractice.dto.UserDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.example.springpractice.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.springpractice.entity.QMember.member;
import static com.example.springpractice.entity.QTeam.team;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ITERABLE;

@Slf4j
//@Rollback(value = false)
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

    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    void fetchJoinNo() {
        entityManager.flush();
        entityManager.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 엔티티의 초기화 여부
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    void fetchJoinYes() {
        entityManager.flush();
        entityManager.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 엔티티의 초기화 여부
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     *
     * select
     *             m1_0.member_id,
     *             m1_0.age,
     *             m1_0.team_id,
     *             m1_0.username
     *         from
     *             member m1_0
     *         where
     *             m1_0.age=(
     *                 select
     *                     max(m2_0.age)
     *                 from
     *                     member m2_0
     *             )
     */
    @Test
    void subQuery1() {
        // alias 가 중복되지 않아야하는 경우
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions.select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(members).extracting("age").containsExactly(50);
    }

    /**
     * 나이가 평균 이상인 회원
     */
    @Test
    void subQueryGoe() {
        // alias 가 중복되지 않아야하는 경우
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub) //35
                ))
                .fetch();

        assertThat(members).extracting("age").containsExactly(40, 50);
    }


    /**
     * 서브쿼리 여러 건 처리, in 사용
     */
    @Test
    void subQueryIn() {
        // alias 가 중복되지 않아야하는 경우
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age) // 30살 이상인 나이들 (30,40,50)
                                .from(memberSub)
                                .where(member.age.goe(30))
                ))
                .fetch();

        assertThat(members).extracting("age").containsExactly(30, 40, 50);
    }

    @Test
    void selectSubQuery() {
        // alias 가 중복되지 않아야하는 경우
        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory.select(
                        member.username,
                        JPAExpressions // static import 가능하지만 냅둠
                                .select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            log.info("tuple= {}", tuple);
        }
    }

    @Test
    void basicCase() {

        // 아래와 같이 db에서 직접 따로 문자열로 전환하고 보여주는 등은 지양 (애플리케이션에서 하기)
        List<String> result = queryFactory
                .select(member.age
                        .when(20).then("20살")
                        .when(30).then("30살")
                        .when(40).then("40살")
                        .otherwise("그 외")
                )
                .from(member)
                .fetch();

        for (String s : result) {
            log.info("age ={}", s);
        }
    }

    @Test
    void complexCase() {

        // 아래와 같이 db에서 직접 따로 문자열로 전환하고 보여주는 등은 지양 (애플리케이션에서 하기)
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(20, 29)).then("20대")
                        .when(member.age.between(30, 39)).then("30대")
                        .when(member.age.between(40, 49)).then("40대")
                        .when(member.age.between(50, 59)).then("50대")
                        .otherwise("그 외")
                ).from(member)
                .fetch();

        for (String s : result) {
            log.info("age ={}", s);
        }
    }

    @Test
    void constant() {

        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            log.info("tuple ={}", tuple);
        }
    }

    @Test
    void concat() {
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue())) // 문자가 아닌 타입들을 문자로 변환 가능 (ENUM 을 처리할때도 자주 사용)
                .from(member)
                .fetch();
        for (String s : result) {
            log.info("member ={}", s);
        }
    }

    @Test
    void simpleProjection() {
        List<String> result1 = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        List<Member> result2 = queryFactory
                .select(member)
                .from(member)
                .fetch();
    }
    @Test
    void tupleProjection() {
        // com.querydsl.core.Tuple
        // Querydsl 에 종속적인 타입이므로 repository 나 DAO 계층 안에서만 쓰도록 한다.
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            log.info("member ={} - {}", username, age);
        }
    }

    @Test
    void findDtoByJPQL() {

        // new operation 방식
        List<MemberDto> result = entityManager
                .createQuery("select " +
                        " new com.example.springpractice.dto.MemberDto(m.username, m.age) " +
                        " from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : result) {
            log.info("memberDto={} ", memberDto);
        }
    }

    @Test
    void findByQuerydsl() {

        //프로퍼티 접근 - Setter (기본생성자, Setter 필요)
        List<MemberDto> result1 = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        // 필드 직접 접근 (getter,setter 필요X)
        // 필드가 private인데 어떻게? - 라이브러리, 자바 리플렉션 등 방법이 있음
        List<MemberDto> result2 = queryFactory.select(
                Projections.fields(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        // 생성자 접근 (파라미터 순서 주의)
        List<MemberDto> result3 = queryFactory.select(
                Projections.constructor(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
    }

    @Test
    void findUserDto1() {

        List<UserDto> result = queryFactory.select(
                        Projections.fields(UserDto.class,
                                member.username.as("name"), // dto의 필드명에 맞춰야함
                                member.age
                        ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            log.info("userDto= {}", userDto); // dto필드명이 다를 경우 무시돼버림, userDto= UserDto(name=null, age=20)
        }
    }

    @Test
    void findUserDto2() {
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory.select(
                        Projections.fields(UserDto.class,
                                member.username.as("name"),

                                // ExpressionUtils.as(source,alias) : 필드나, 서브 쿼리에 별칭 적용
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(memberSub.age.max())
                                                .from(memberSub),"age")
                                )
                        )
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            log.info("userDto= {}", userDto);
        }
    }

    @Test
    void findUserDto3() {
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory.select(
                        Projections.constructor(UserDto.class,
                                // 이름이 아닌 순서,타입을 보고 매칭 (문제가 있을 시 런타임에러)
                                member.username,
                                member.age
                                )
                )
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            log.info("userDto= {}", userDto);
        }
    }

    /**
     * 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이지만
     * DTO 자체가 Querydsl의 의존성을 가지게 되는 문제를 가짐
     * - DTO에 QueryDSL 어노테이션을 유지 해야 하는 점
     * - DTO까지 Q 파일을 생성해야 하는 단점
     */

    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
    }

    @Test
    void distinct() {
        List<String> result = queryFactory
                .select(member.username).distinct()
                .from(member)
                .fetch();
    }

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member4";
        Integer ageParam = 50;
        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCondition, Integer ageCondition) {

        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCondition != null) {
            builder.and(member.username.eq(usernameCondition));
        }
        if (ageCondition != null) {
            builder.and(member.age.eq(ageCondition));
        }

        return queryFactory.selectFrom(member)
                .where(builder)
                .fetch();
    }
    
    @Test
    void dynamicQuery_WhereParam() {
        String usernameParam = "member4";
        Integer ageParam = 50;
        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCondition, Integer ageCondition) {
        return queryFactory
                .selectFrom(member)
                //.where(usernameEq(usernameCondition), ageEq(ageCondition))
                .where(allEq(usernameCondition, ageCondition))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCondition) {
        if(usernameCondition == null) {
            return null; // where 절에 null 이 들어가면 무시됨
        }
            return member.username.eq(usernameCondition);
    }

    private BooleanExpression ageEq(Integer ageCondition) {
        if(ageCondition == null){
            return null;
        }
        return member.age.eq(ageCondition);
    }

    private Predicate allEq(String usernameCondition, Integer ageCondition){
        return usernameEq(usernameCondition).and(ageEq(ageCondition));
    }

    //@Commit
    @Test
    void bulkUpdate() {
        // 영향을 받은 row 수 반환
        long count = queryFactory
                .update(member)
                .set(member.username, "젊은이")
                .where(member.age.lt(40))
                .execute();

        entityManager.flush();
        entityManager.clear();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void bulkAdd() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(-1))
                .execute();

        entityManager.flush();
        entityManager.clear();

        assertThat(count).isEqualTo(4);
    }

    @Test
    void bulkMultiple() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.multiply(2))
                .execute();

        entityManager.flush();
        entityManager.clear();

        assertThat(count).isEqualTo(4);
    }

    @Test
    void bulkDelete() {
        long count = queryFactory
                .delete(member)
                .where(member.age.lt(30))
                .execute();

        entityManager.flush();
        entityManager.clear();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void dialect() {
        String result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetchFirst();
    }
}
