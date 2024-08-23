package com.example.springpractice.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest // aop 동작 등을 보기 위해 SpringBootTest 로 진행
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("aop class={}", basicService.getClass()); // aop class=class com.example.springpractice.apply.TxBasicTest$BasicService$$SpringCGLIB$$0
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.nonTx();
    }


    @TestConfiguration
    static class TxApplyBasicConfig {

        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {
        /**
         * 해당 클래스는  @Transactional 이 있기때문에 트랜잭션 AOP가 프록시를 만들어서 스프링 컨테이너에 등록
         * 실제 BasicService 객체가 아닌 BasicService 의 프록시가 스프링 빈에 등록
         * 프록시는 내부에 실제 BasicService 를 참조
         * 실제 BasicService 를 상속 (다형성 활용 가능)
         */

        @Transactional
        public void tx(){
            log.info("call tx");
            
            // 현재 돌아가는 쓰레드 안에서의 트랜잭션 존재여부를 바로 확인가능
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}", txActive);
        }

        public void nonTx(){
            log.info("call nonTx");

            // 현재 돌아가는 쓰레드 안에서의 트랜잭션 존재여부를 바로 확인가능
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active={}", txActive);
        }
    }
}
