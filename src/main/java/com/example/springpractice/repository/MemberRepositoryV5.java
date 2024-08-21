package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JdbcTemplate 사용
 * 
 * JdbcTemplate사용 시 JDBC로 개발할 때
 * 발생하는 반복, 커넥션 동기화 , 스프링 예외 변환 등의 문제를 자동으로 해결해줌
 * 템플릿 콜백 패턴은 후에 고급편 참고
 */
@Slf4j
//@RequiredArgsConstructor
public class MemberRepositoryV5 implements MemberRepository {
    
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";

        int update = template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }


    @Override
    public Member findById(String memberId) {

        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);
    }


    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from member";
        template.update(sql);
    }

    private RowMapper<Member> memberRowMapper() {
        
        // resultSet, 행 번호
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
