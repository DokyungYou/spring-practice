package com.example.springpractice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
drop table member if exists cascade;
create table member (
    member_id varchar(10),
    money integer not null default 0,
primary key (member_id)
);
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String memberId;
    private int money;
}
