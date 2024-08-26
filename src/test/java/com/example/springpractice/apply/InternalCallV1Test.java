package com.example.springpractice.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService; // 프록시 객체 주입

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
        // InternalCallV1Test$CallService$$SpringCGLIB$$0
    }

    @Test
    void internalCall(){
        callService.internal();
    }

    @Test
    void externalCall(){
        callService.external();
    }


    @TestConfiguration
    static class InternalCallV1TestConfig {

        @Bean
        CallService callService(){
            return new CallService();
        }
    }

    static class CallService {

        /**
         * @transactional self-invocation
         * 사실상 대상 객체의 다른 메서드를 호출하는 대상 객체 내의 메서드는 런타임에 실제 트랜잭션으로 이어지지 않음
         * 
         * 별도의 참조가 없다면 앞에는 자기 자신을 가르키는 this가 붙게됨 (생략가능)
         * 이 this는 실제 대상 객체(target)의 인스턴스를 뜻한다.
         * -> target 에 있는 메서드를 직접 호출
         * -> 프록시를 거치지 않음
         * -> 트랜잭션 적용 불가
         */
        public void external(){
            log.info("call external");
            printTxInfo();

            // @Transactional이 적용된 메서드를 다른 메서드 내부에서 호출한 상황
            internal();
        }

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);

            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", readOnly);
        }
    }
}
