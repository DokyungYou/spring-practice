package com.example.springpractice.exception.translator;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.ex.MyDbException;
import com.example.springpractice.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.example.springpractice.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);

        repository.deleteAll();
    }

    @Test
    void duplicateKeySave(){
        service.create("myId");
        service.create("myId"); // 중복 ID 저장 시도
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;

        void create(String memberId){

            try{
                repository.save(new Member(memberId, 0));
                log.info("saveId={}", memberId);
            } catch (MyDuplicateKeyException e){
                log.info("키 중복, 복구 시도");
                 String retryId = generateNewId(memberId);
                 log.info("retryId={}", retryId);
                 repository.save(new Member(retryId, 0));
            } catch (MyDbException e){
                
                // 아래와 같이 하지 말고 복구할 수 없는 예외로그는 공통으로 예외를 처리하는 곳에서 예외 로그를 남기도록 하자
                log.info("데이터 접근 계층 예외", e);
                throw e;
            }

        }
    }

    private static String generateNewId(String memberId){
        return memberId + new Random().nextInt(10000);
    }

    @RequiredArgsConstructor
    static class Repository {

        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = dataSource.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, member.getMemberId());
                preparedStatement.setInt(2, member.getMoney());
                preparedStatement.executeUpdate();

                return member;

            } catch (SQLException e) {
                log.info("errorCode={}", e.getErrorCode());

                // h2 db
                if (e.getErrorCode() == 23505) {
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);

            } finally {
                JdbcUtils.closeStatement(preparedStatement);
                JdbcUtils.closeConnection(connection);
            }
        }

        public void deleteAll() {
            String sql = "delete from member";
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = dataSource.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new MyDbException(e);

            } finally {
                JdbcUtils.closeStatement(preparedStatement);
                JdbcUtils.closeConnection(connection);
            }
        }
    }
}
