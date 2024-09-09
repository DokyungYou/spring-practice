package com.example.springpractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id","name"}) // 연관관계가 있는 필드는 제외 (무한루프문제)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 막는 용 (JPA 스팩상 private 는 불가)
@Getter @Setter // Setter 지양
@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name){
        this.name = name;
    }
}
