package com.example.springpractice.domain;

import lombok.*;

/*
drop table member if exists cascade;
create table member (
    member_id varchar(10),
    money integer not null default 0,
primary key (member_id)
);
*/

@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String memberId;
    private int money;
}
