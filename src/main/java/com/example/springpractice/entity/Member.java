package com.example.springpractice.entity;

import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username")
@Getter @Setter // setter 지양
@ToString(of = {"id","username","age"}) // team은 무한루프 때문에 제외 (가급적 연관관계 없는 내부 필드만)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null){
            changeTeam(team);
        }
    }


    /** Mebmer <-> Team
     * 연관관계 편의 메서드
     */
    private void changeTeam(Team team) {
        // 양방향 연관관계 한번에 세팅
        this.team = team;
        team.getMembers().add(this);
    }
}