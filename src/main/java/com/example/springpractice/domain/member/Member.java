package com.example.springpractice.domain.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {

    private Long id;

    private String loginId;
    private String name;
    private String password;

}
