package com.example.springpractice.controller;

import com.example.springpractice.entity.Member;
import com.example.springpractice.entity.Team;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitMember {

    private final InitMemberService initMemberService;

    /**
     * @PostConstruct는 빈 초기화 시점에 호출되기 때문에 아직 트랜잭션 프록시가 적용되지 않아,
     * @Transactional과 함께 사용하면 트랜잭션이 제대로 동작하지 않을 수 있으므로 분리
     */
    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager entityManager;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            entityManager.persist(teamA);
            entityManager.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team seletedTeam = i % 2 == 0 ? teamA :teamB;
                entityManager.persist(new Member("member" + i, i,seletedTeam));
            }

        }

    }
}
