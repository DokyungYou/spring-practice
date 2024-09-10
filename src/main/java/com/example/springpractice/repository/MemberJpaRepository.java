package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberSearchCondition;
import com.example.springpractice.dto.MemberTeamDto;
import com.example.springpractice.dto.QMemberTeamDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.QMember;
import com.example.springpractice.entity.QTeam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.example.springpractice.entity.QMember.member;
import static com.example.springpractice.entity.QTeam.*;

@RequiredArgsConstructor
@Repository
public class MemberJpaRepository { 
//   JPA 과 Querydsl 비교

    // TODO 엔티티매니저의 동시성 문제에 대한 부분 공부 (13.1 트랜잭션 범위의 영속성 컨텍스트)
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory; // 따로 빈으로 등록해놨음

//    public MemberJpaRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//        this.queryFactory = new JPAQueryFactory(entityManager);
//    }


    public void save(Member member){
        entityManager.persist(member);
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public List<Member> findAll(){
        return entityManager
                .createQuery("select m from Member m", Member.class)
                .getResultList();
    }


    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername(String username){
        return entityManager
                .createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsername_Querydsl(String username){
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }


    /**
     * 조건이 모두 null 일 경우 모든 데이터를 다 가져오기때문에, 데이터가 많을 때의 상황 주의
     * 이런 동적 쿼리를 짤 때는 웬만하면 기본조건을 넣거나 limit를 넣어주어야한다.
     */
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){

        BooleanBuilder builder = new BooleanBuilder();
        // null 이거나 공백문자 검사
        if (StringUtils.hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (StringUtils.hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }

        return queryFactory.select(new QMemberTeamDto(
                    member.id.as("memberId"),
                    member.username,
                    member.age,
                    team.id.as("teamId"),
                    team.name.as("teamName")
                    ))
                    .from(member)
                    .leftJoin(member.team, team)
                    .where(builder)
                    .fetch();
    }

}
