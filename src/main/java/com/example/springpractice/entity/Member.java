package com.example.springpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@ToString(of = {"id","username","age"}) // 연관관계가 있는 필드는 제외 (무한루프문제)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 막는 용 (JPA 스팩상 private 는 불가)
@Getter @Setter // Setter 지양
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null){
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        // 양방향 연관관계 한번에 처리 (연관관계 편의 메서드)
        this.team = team;
        team.getMembers().add(this);
    }
}
