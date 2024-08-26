package com.example.springpractice.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LogRepository {

    private final EntityManager entityManager;

    // 테스트 코드의 주석 참고 후 Transactional 를 세팅
    @Transactional
    public void save(Log logMessage){
        log.info("log 저장");
        entityManager.persist(logMessage);

        if(logMessage.getMessage().contains("로그예외")){
            log.info("로그 저장 시 예외 발생");
            throw new RuntimeException("예외 발생"); // 언체크예외는 롤백
        }
    }

    public Optional<Log> find(String message) {
        return entityManager.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList()
                .stream()
                .findAny();
    }
}
