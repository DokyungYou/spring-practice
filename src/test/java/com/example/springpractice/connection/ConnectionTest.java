package com.example.springpractice.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.springpractice.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        // 서로 다른 db 커넥션 두개를 얻게 됨
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        
        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverManagerDataSource - 항상 새로운 커넥션 획득
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }


    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(5); // 디폴트가 10개
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        
        /*
        커넥션 풀에서 커넥션을 생성하는 작업은 
        애플리케이션 실행 속도에 영향을 주지 않기 위해 별도의 쓰레드에서 작동
        별도의 쓰레드에서 동작하기 때문에 테스트가 먼저 종료돼버림
        Thread.sleep 을 통해 대기 시간을 주어야 쓰레드 풀에 커넥션이 생성되는 로그 확인가능
        */
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {

        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();
        Connection connection3 = dataSource.getConnection();
        Connection connection4 = dataSource.getConnection();
        Connection connection5 = dataSource.getConnection(); // MyPool - Connection not added, stats (total=5, active=5, idle=0, waiting=0)

        /*
        MyPool - Connection not added, stats (total=5, active=5, idle=0, waiting=1) 로 나오고 계속 로딩
        pool이 확보가 될 때까지 대기하게 됨
        이런 상황에 대한 설정을 따로 할 수 있음(공식 사이트 등 참조)
         */
       // Connection connection6 = dataSource.getConnection();

        log.info("connection={}, class={}", connection1, connection1.getClass());
        log.info("connection={}, class={}", connection2, connection2.getClass());
    }
}


