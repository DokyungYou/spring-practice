package com.example.springpractice.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService levelService;

    @Test
    void orderTest(){
        levelService.write();
        levelService.read();
    }



    @TestConfiguration
    static class TxLevelTestConfig {

        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    // 기본적으로 트랜잭션은 읽기와 쓰기를 다 할 수 있다?
    @Slf4j
    @Transactional(readOnly = true) // 읽기전용 트랜잭션 (쓰기 작업할 때 문제가 생기지 않게)
    static class LevelService {

        @Transactional(readOnly = false)
        public void write(){
            log.info("call write");
            printTxInfo();
        }

        public void read(){
            log.info("call read");
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
