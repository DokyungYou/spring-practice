package com.example.springpractice.web.member;

import com.example.springpractice.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberAddForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    public Member toMember(){
        Member member = new Member();
        member.setLoginId(this.loginId);
        member.setName(this.getName());
        member.setPassword(this.getPassword());

        return member;
    }
}
