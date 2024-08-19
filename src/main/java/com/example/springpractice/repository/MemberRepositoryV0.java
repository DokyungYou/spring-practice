package com.example.springpractice.repository;

import com.example.springpractice.connection.DBConnectionUtil;
import com.example.springpractice.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null; // 데이터베이스에 전달할 sql문과 파라미터로 전달할 데이터 준비


        try{
            connection = DBConnectionUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            
            // 파라미터를 sql문의 ?에 바인딩 (타입정보, 데이터)
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());

            // 준비된 것이 실제 db에 실행
            preparedStatement.executeUpdate(); // 영향받은 DB row 수를 반환 (현재 코드에서는 1이 반환될 것임)
            return member;

        }catch (SQLException e){
            log.error("db error", e);
            throw  e; // 다시 던져..?

        }finally {
            // 리소스 정리 필수
            // 예외 발생 유무와 상관없이 무조건 수행되어야함
            close(connection, preparedStatement, null);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet){ // ResultSet 은 결과를 조회할 때 사용
        if(resultSet != null){
            try{
                resultSet.close();
            }catch (SQLException e){
                log.error("error", e);
            }
        }

        if(statement!= null){
            try{
                statement.close();
            }catch (SQLException e){
                log.error("error", e);
            }
        }

        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                log.error("error", e);
            }
        }
    }
}
