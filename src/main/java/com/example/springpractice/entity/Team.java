package com.example.springpractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter // setter 지양
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"}) // (가급적 연관관계 없는 내부 필드만)
@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // fk가 없는 쪽에 mappedBy 세팅하는 것을 권장
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
