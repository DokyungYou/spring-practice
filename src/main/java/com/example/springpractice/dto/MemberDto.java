package com.example.springpractice.dto;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String username;
    private int age;

}
