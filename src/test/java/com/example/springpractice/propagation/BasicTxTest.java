package com.example.springpractice.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @TestConfiguration
    static class Config {

        /**
         * 원래는 스프링이 트랜잭션 매니저도 자동으로 등록해주지만,
         * 아래와 같이 빈을 직접 등록하게되면, 직접 등록한 것을 사용
         */
        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit(){
        log.info("트랜잭션 시작");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 커밋 시작");
        transactionManager.commit(transaction);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback(){
        log.info("트랜잭션 시작");
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 롤백 시작");
        transactionManager.rollback(transaction);
        log.info("트랜잭션 롤백 완료");
    }

    @Test
    void double_commit(){
        log.info("트랜잭션1 시작");
        TransactionStatus transaction1 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        transactionManager.commit(transaction1);

        log.info("트랜잭션2 시작");
        TransactionStatus transaction2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋");
        transactionManager.commit(transaction2);
    }

    @Test
    void double_commit_rollback(){
        log.info("트랜잭션1 시작");
        TransactionStatus transaction1 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        transactionManager.commit(transaction1);

        log.info("트랜잭션2 시작");
        TransactionStatus transaction2 = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 롤백");
        transactionManager.rollback(transaction2);
    }

    @Test
    void inner_commit(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outerTransaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outerTransaction.isNewTransaction()); // 신규 트랜잭션 여부

        log.info("내부 트랜잭션 시작");
        TransactionStatus innerTransaction = transactionManager.getTransaction(new DefaultTransactionAttribute()); // Participating in existing transaction
        log.info("inner.isNewTransaction()={}", innerTransaction.isNewTransaction());
        log.info("내부 트랜잭션 커밋");

        // 참고로 하나의 커넥션에서 커밋은 한번만 호출할 수 있음 (호출 시 해당 커넥션은 끝나는거임)
        // 여기서 실제 커밋을 호출하면 안됨 (물리 트랜잭션은 외부 트랙잭션을 종료할 때까지 이어져야함)
        transactionManager.commit(innerTransaction); // 여기서는 실제로 커밋을 하지 않는다. (트랜잭션 매니저는 커밋 시점에 신규 트랜잭션 여부에 따라 다르게 동작)

        
        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outerTransaction); // isNewTransaction 가 true 인 커넥션이기때문에 실제 commit 호출 (물리 커밋)
    }
}
