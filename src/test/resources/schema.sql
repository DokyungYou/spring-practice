drop table if exists item CASCADE;
create table item
(
    id        bigint generated by default as identity,
    item_name varchar(10),
    price     integer,
    quantity  integer,
    primary key (id)
);

// id  bigint generated by default as identity -> 기본 키 생성을 데이터베이스에 위임하는 방식 (MySQL 의 Auto Increment 와 동일)