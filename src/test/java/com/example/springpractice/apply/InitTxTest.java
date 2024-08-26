package com.example.springpractice.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;

    @Test
    void go(){
        // 초기화 코드는 스프링이 초기화 시점에 호출
    }

    @TestConfiguration
    static class InitTxTestConfig {

        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    @Slf4j
    static class Hello {


        /**
         * @PostConstruct, @Transactional 를 같이 사용하면 트랜잭션 적용 X
         * AOP 가 적용되기 전에 @PostConstruct 가 먼저 호출될 수 있음
         *
         * 직접 따로 해당 메서드를 호출하면 트랜잭션 적용되기는 함
         */
        @PostConstruct
        @Transactional
        public void initV1(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx isActive={}" ,isActive);
        }


        /** 스프링 컨테이너가 완전히 다 준비돼서 올라왔을 때 호출
         *  (빈 뿐만 아니라 AOP, 트랜잭션 등 다 적용해서 완성이 됐을 떄 )
         */
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init ApplicationReadyEvent tx isActive={}" ,isActive);
        }
    }
}
