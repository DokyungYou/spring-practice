package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.ex.MyDbException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크예외 -> 언체크예외 로 변경
 * MemberRepository 인터테이스 사용
 * throws SQLException 제거
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV4_1 implements MemberRepository {

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null; // 데이터베이스에 전달할 sql문과 파라미터로 전달할 데이터 준비


        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // 파라미터를 sql문의 ?에 바인딩 (타입정보, 데이터)
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());

            // 준비된 것이 실제 db에 실행
            preparedStatement.executeUpdate(); // 영향받은 DB row 수를 반환 (현재 코드에서는 1이 반환될 것임)
            return member;

        }catch (SQLException e){

            // 언체크 예외로 다시 던지기 (원래 터졌던 예외를 담아서)
            throw  new MyDbException(e);

        }finally {
            // 리소스 정리 필수
            // 예외 발생 유무와 상관없이 무조건 수행되어야함
            close(connection, preparedStatement, null);
        }
    }


    @Override
    public Member findById(String memberId) {

        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);
            resultSet = preparedStatement.executeQuery();

            // 내부에 커서라는게 있는데  resultSet.next 를 한번 호출을 해줘야 실제 데이터가 있는 것부터 시작
            if(resultSet.next()){
                Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberid=" + memberId);
            }

        }catch (SQLException e){
            // 언체크 예외로 다시 던지기 (원래 터졌던 예외를 담아서)
            throw  new MyDbException(e);

        }finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            int resultSize = preparedStatement.executeUpdate();
            log.info("resultSize={}", resultSize);

        } catch (SQLException e) {
            // 언체크 예외로 다시 던지기 (원래 터졌던 예외를 담아서)
            throw  new MyDbException(e);

        } finally {
            close(connection, preparedStatement, null);
        }
    }


    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // 언체크 예외로 다시 던지기 (원래 터졌던 예외를 담아서)
            throw  new MyDbException(e);

        } finally {
            close(connection, preparedStatement, null);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "delete from member";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // 언체크 예외로 다시 던지기 (원래 터졌던 예외를 담아서)
            throw  new MyDbException(e);

        } finally {
            close(connection, preparedStatement, null);
        }
    }

    /**
     * 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용해야함
     * close로 직접 닫으면 커넥션이 유지 되지 않는다.
     * 해당 커넥션은 이 후 로직, 트랜잭션을 종료할때까지 살아있어야함 (커밋, 롤백할때까지)
     */
    private void close(Connection connection, Statement statement, ResultSet resultSet){
        // close 순서 중요
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);

        // 트랜잭션을 사용하기 위해 동기화된 커넥션은 닫지 않고 그대로 유지
        // 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫음
        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    private Connection getConnection() throws SQLException {
        // 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용해야함
        Connection connection = DataSourceUtils.getConnection(dataSource);
        
        log.info("get connection={}, class={}", connection, connection.getClass());
        return connection;
    }
}
