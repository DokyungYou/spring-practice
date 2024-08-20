package com.example.springpractice.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unchecked(){
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx(){
        Controller controller = new Controller();

        try{
            controller.request();
        }catch (Exception e) {
            //e.printStackTrace(); // 지양


            // Repository 의 call() 에서  언체크예외로 다시 던질 때 기존 체크예외를 담아서 던졌다.
            // 로그로 찍을 때  Caused by: java.sql.SQLException: ex
            // 처음 원인이었던 예외정보를 포함해서 볼 수 있다. 중요
            log.info("ex", e);

        }

    }

    static class Controller {
        Service service = new Service();

        public void request(){
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() {
            try{
                runSQL();
            }catch (SQLException e){

                // 체크예외 -> 언체크예외로 바꿔치기 (전환 시에 기존 예외 포함 필수)
                // 예외를 던질 때 기존 예외를 넣어줘야 스택 트레이스도 같이 확인 가능
                throw new RuntimeSQLException(e);
            }

        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(String message) {
            super(message);
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
