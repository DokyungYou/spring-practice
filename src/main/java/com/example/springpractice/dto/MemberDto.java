package com.example.springpractice.dto;

import com.example.springpractice.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    // 의존관계에 있어 엔티티는 DTO를 의존하지 않아야함
    // DTO 가 엔티티를 의존하는 것은 괜찮음
    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        //this.teamName = member.getTeam().getName();
    }
}
