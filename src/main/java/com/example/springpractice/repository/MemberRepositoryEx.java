package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;

import java.sql.SQLException;

public interface MemberRepositoryEx {

    /**
     * 특정 기술을 쉽게 변경하기 위해 인터페이스를 도입했으나,
     * SQLException 같은 특정 구현 기술에 종속적인 체크 예외를 사용하면, 
     * 인터페이스에도 해당 예외를 포함해야함 (물론 인터페이스에서 Exception 같은 상위 예외로 던질수도 있지만 지양)
     * JDBC 기술에 종속적인 인터페이스여서 결국 다른 기술로 변경 시 인터페이스를 변경해야함
     * 구현체를 쉽게 변경하기 위한 목적을 벗어나게 됨
     */

    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
    void deleteAll() throws SQLException;
}
