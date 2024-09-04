package com.example.springpractice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter //Setter 지양
@NoArgsConstructor
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded
    private Address address;


    /** @JsonIgnore 아래 api 실습시에만 적용하기
     * - @GetMapping("/v1/members")
     * - @GetMapping("/v1/simple-order")
     *
     * 프레젠테이션 계층을 위한 로직이 추가돼버린 상황 (엔티티에서 의존관계가 나가버림)
     * 양방향으로 의존관계가 걸리면서 애플리케이션 수정이 어렵게 된 상황
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member") // order 테이블에 있는 member 필드에 의해 mapping
    private List<Order> orders = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }
}
