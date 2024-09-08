package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager entityManager;     // 같은 트랜잭션이면 같은 엔티티 매니저가 동작

    @Test
    public void testMember() {

        log.info("memberRepository = {}", memberRepository.getClass()); // class jdk.proxy3.$Proxy131
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // 바로 꺼내는 방식 지양
        
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // JPA 엔티티 동일성 보장 (같은 트랜잭션 내에서 영속성 컨텍스트의 동일성 보장)
        }


    @Test
    public void basicCRUD(){
        Member member1 = new Member("멤버1");
        Member member2 = new Member("멤버2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();// 바로 get() 지양
        Member findMember2 = memberRepository.findById(member2.getId()).get();// 바로 get() 지양
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        assertThat(memberRepository.count()).isEqualTo(0);

    }

    @Test
    public void findByUserNameAgeGreaterThen() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("멤버", 20);
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getAge()).isEqualTo(30);
        assertThat(members.get(0).getUsername()).isEqualTo("멤버");
    }

    @Test
    public void findHelloBy() {
        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findHelloBy();
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void findTop3HelloBy() {
        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        Member member3 = new Member("멤버", 30);
        Member member4 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> members = memberRepository.findTop3HelloBy();
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    void testNamedQuery() {

        Member member1 = new Member("멤버",10);
        Member member2 = new Member("멤버", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findNamedByUsername("멤버");
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void testQuery() {

        Member member1 = new Member("멤버1",10);
        Member member2 = new Member("멤버2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members1 = memberRepository.findUserByUsernameAndAge("멤버1",10);
        List<Member> members2 = memberRepository.findUserByUsernameAndAge("멤버2",30);
        assertThat(members1.get(0)).isEqualTo(member1);
        assertThat(members2.get(0)).isEqualTo(member2);
    }

    @Test
    void findUsernameList() {

        Member member1 = new Member("멤버1",10);
        Member member2 = new Member("멤버2", 30);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Set<String> nameSet = new HashSet<>(Set.of(member1.getUsername(), member2.getUsername()));

        List<String> userNameList = memberRepository.findUsernameList();
        for (String name : userNameList) {
            assertTrue(nameSet.contains(name));
        }

    }

    @Test
    void findMemberDtoList() {

        Team team1 = new Team("팀1");
        Team team2 = new Team("팀2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("멤버1",10, team1);
        Member member2 = new Member("멤버2", 30, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> memberDtoList = memberRepository.findMemberDtoList();
        for (MemberDto memberDto : memberDtoList) {
            log.info("memberDto= {}", memberDto);
        }

    }

    @Test
    void findByNames() {

        List<String> names = new ArrayList<>(List.of("멤버1", "멤버2","멤버3","멤버4"));

        for (String name : names) {
            memberRepository.save(new Member(name));
        }

        List<Member> findMembers = memberRepository.findByNames(names);
        for (Member findMember : findMembers) {
            log.info("findMember= {}", findMember);
        }
    }

    @Test
    void returnTypeTest() {
        Member member1 = new Member("멤버1");
        Member member2 = new Member("중복이름");
        Member member3 = new Member("중복이름");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findListByUsername(member1.getUsername());
        Member findMember = memberRepository.findMemberByUsername(member1.getUsername());
        Optional<Member> memberOptional = memberRepository.findMemberOptionalByUsername(member1.getUsername());

        assertThat(members.get(0)).isEqualTo(member1);
        assertThat(findMember).isEqualTo(member1);
        assertThat(memberOptional.get()).isEqualTo(member1);


        // Spring Data Jpa 가 원인인 예외를 Spring Framework Exception 으로 변환 후 반환
        // org.springframework.dao.IncorrectResultSizeDataAccessException: Query did not return a unique result: 2 results were returned
        // Caused by: org.hibernate.NonUniqueResultException: Query did not return a unique result: 2 results were returned
        //Optional<Member> memberOptionalByUsername = memberRepository.findMemberOptionalByUsername("중복이름");
    }

    @Test
    void paging_page() {

        memberRepository.save(new Member("중학생0",11));
        memberRepository.save(new Member("중학생1",15));
        memberRepository.save(new Member("중학생2",15));
        memberRepository.save(new Member("중학생3",15));
        memberRepository.save(new Member("중학생4",15));
        memberRepository.save(new Member("중학생5",15));


        int age = 15;

        // 페이지는 1이 아닌 0부터 시작
        PageRequest pageRequest1 =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        PageRequest pageRequest2 =
                PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        PageRequest pageRequest3 =
                PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page1 = memberRepository.findPageByAge(age, pageRequest1);
        Page<Member> page2 = memberRepository.findPageByAge(age, pageRequest2);
        Page<Member> page3 = memberRepository.findPageByAge(age, pageRequest3);

        // 총 페이지, 데이터 개수
        assertThat(page1.getTotalPages()).isEqualTo(2);
        assertThat(page1.getTotalElements()).isEqualTo(5);

        // 해당 페이지에 있는 데이터 개수
        assertThat(page1.getContent().size()).isEqualTo(3);
        assertThat(page2.getContent().size()).isEqualTo(2);
        assertThat(page3.getContent().size()).isEqualTo(0);

        // 해당 페이지 번호
        assertThat(page1.getNumber()).isEqualTo(0);
        assertThat(page2.getNumber()).isEqualTo(1);
        assertThat(page2.getNumber()).isEqualTo(2); // 데이터가 없는 페이지

        // 페이지를 유지하면서 엔티티 -> DTO 변환
        Page<MemberDto> map = page1.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));

    }

    @Test
    void paging_slice() {

        memberRepository.save(new Member("중학생0",11));
        memberRepository.save(new Member("중학생1",15));
        memberRepository.save(new Member("중학생2",15));
        memberRepository.save(new Member("중학생3",15));
        memberRepository.save(new Member("중학생4",15));
        memberRepository.save(new Member("중학생5",15));


        int age = 15;

        // 페이지는 1이 아닌 0부터 시작
        PageRequest pageRequest1 =
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        PageRequest pageRequest2 =
                PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        PageRequest pageRequest3 =
                PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page1 = memberRepository.findSliceByAge(age, pageRequest1);
        Slice<Member> page2 = memberRepository.findSliceByAge(age, pageRequest2);
        Slice<Member> page3 = memberRepository.findSliceByAge(age, pageRequest3);


        // 해당 페이지에 있는 데이터 개수
        assertThat(page1.getContent().size()).isEqualTo(3);
        assertThat(page2.getContent().size()).isEqualTo(2);
        assertThat(page3.getContent().size()).isEqualTo(0);

        // 해당 페이지 번호
        assertThat(page1.getNumber()).isEqualTo(0);
        assertThat(page2.getNumber()).isEqualTo(1);
        assertThat(page2.getNumber()).isEqualTo(1); // 데이터가 없는 페이지

    }


    // TODO 복습 & 정리 + JPA 기본편 복습..
    @Test
    public void bulkUpdate() {

        memberRepository.save(new Member("회원1", 10));
        memberRepository.save(new Member("회원2", 110));
        memberRepository.save(new Member("회원3", 120));
        memberRepository.save(new Member("회원4", 130));
        memberRepository.save(new Member("회원5", 14));

        // bulk 연산 시 영속성 컨텍스트를 무시하고 바로 db에 적용하고, 영속성 컨텍스트는 그 사실을 모른다.
        // bulk 연산 후엔 영속성 컨텍스트를 다 비워야한다.
        int resultCount = memberRepository.bulkAgePlus(100);
        Member member = memberRepository.findMemberByUsername("회원4");
        log.info("age of member ={}", member.getAge()); // 130,  @Modifying(clearAutomatically = true) 적용 후엔 131

        // entityManager.clear(); // @Modifying(clearAutomatically = true) 적용 시 자동 적용

        Member findMember = memberRepository.findMemberByUsername("회원4");
        log.info("age of findMember4 ={}", findMember.getAge()); //131

        assertThat(resultCount).isEqualTo(3);
    }


    /**
     * (1)
     *     select
     *         m1_0.member_id,
     *         m1_0.age,
     *         m1_0.team_id,
     *         m1_0.username
     *     from
     *         member m1_0
     *
     * (n) team을 가진 member 개수만큼 반복
     *    select
     *         t1_0.team_id,
     *         t1_0.name
     *     from
     *         team t1_0
     *     where
     *         t1_0.team_id=?
     *
     * 
     *  lazy 확인하려면 직접 오버라이딩한 findAll 주석처리하기
     */
    @Test
    void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 15, teamA);
        Member member2 = new Member("member2", 55, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        List<Member> members = memberRepository.findAll(); // 일단 Member만 db에서 가져온다. (Lazy인 team은 프록시)
        for (Member member : members) {
            log.info("member= {}", member);
            log.info("member.team class ={}",member.getTeam().getClass()); // Team$HibernateProxy$CNM0jSrO
            log.info("member.team.getTeam ={}",member.getTeam().getName()); //.getName() 하는 순간 실제 쿼리문 날라가고 프록시 초기화);

            /*
            지연로딩 여부는 위와 같이 클래스를 직접확인하는 방법도 있지만, 아래와 같은 방법도 있다.

            Hibernate 기능으로 확인
            Hibernate.isInitialized(member.getTeam()); - boolean 반환

            JPA 표준 방법으로 확인
            PersistenceUnitUtil util =
                    entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
            util.isLoaded(member.getTeam());  - boolean 반환
            */
        }

    }

    @Test
    void findMemberFetch() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 15, teamA);
        Member member2 = new Member("member2", 55, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();

        List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            log.info("member= {}", member);
            log.info("member.team class ={}",member.getTeam().getClass()); // Team (실제 객체)
            log.info("member.team.getTeam ={}",member.getTeam().getName());

            /*
            지연로딩 여부는 위와 같이 클래스를 직접확인하는 방법도 있지만, 아래와 같은 방법도 있다.

            Hibernate 기능으로 확인
            Hibernate.isInitialized(member.getTeam()); - boolean 반환

            JPA 표준 방법으로 확인
            PersistenceUnitUtil util =
                    entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
            util.isLoaded(member.getTeam());  - boolean 반환
            */
        }

    }
}