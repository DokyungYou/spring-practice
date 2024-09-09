package com.example.springpractice.dto;

import lombok.*;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;
    private int age;
}
