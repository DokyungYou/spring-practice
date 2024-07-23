package com.example.springpractice.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public class Member {

    private Long id;
    private String name;
    private Grade grade;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }


//    @Override
//    public String toString() {
//        return "Member{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", grade=" + grade +
//                '}';
//    }
}
