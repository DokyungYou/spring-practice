package com.example.springpractice.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedTest {

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();

        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUnCheckedException.class);

    }

    /**
     * RunException 을 상속받은 예외는 언체크예외가 됨
     */
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    /**
     * UnChecked 예외는 
     * 예외를 잡거나 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던져짐
     */
    static class Service {
        private final Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            try{
                repository.call();
            }catch (MyUnCheckedException e) {
                // 예외를 stackTrace 로 출력할 때는 마지막 파라미터에 exception 만 넣으면 됨
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 자연스럽게 상위로 넘어감
         * 체크 예외와 다르게 throws 예외 생략 가능
         */
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() { // throws 생략 가능 (인지하고 싶으면 써도 됨)
            throw new MyUnCheckedException("ex");
        }
    }
}
