package com.example.springpractice.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection; // JDBC 표준 인터페이스가 제공하는 커넥션
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.springpractice.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    
    public static Connection getConnection(){

        try{
            // 라이브러리에 있는 데이터베이스 드라이버를 찾아서 드라이버가 제공하는 커넥션을 반환
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("connection = {}, class={}", connection, connection.getClass());
            return connection;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
