package com.example.springpractice;

import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.example.springpractice.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.example.springpractice.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

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
}
