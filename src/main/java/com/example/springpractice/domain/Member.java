package com.example.springpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter //Setter 지양
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // order 테이블에 있는 member 필드에 의해 mapping
    private List<Order> orders = new ArrayList<>();
}
