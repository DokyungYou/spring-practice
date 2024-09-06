package com.example.springpractice.dto;

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

}
