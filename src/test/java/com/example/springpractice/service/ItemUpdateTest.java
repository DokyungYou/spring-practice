package com.example.springpractice.service;

import com.example.springpractice.domain.item.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ItemUpdateTest { // 변경 감지와 병합 관련 테스트

    @Autowired
    EntityManager entityManager;

    @Test
    void updateTest(){
        Book book = entityManager.find(Book.class, 1L);

        // Transaction
        book.setPrice(10000); // 변경


        // 트랜잭션 커밋 시에 "변경을 감지" 해서 업데이트 쿼리를 자동으로 생성해서 날리고 db에 반영 (더티 체킹)
        // 해당 매커니즘을 기본으로 JPA 엔티티를 변경할 수 있다.
        // Transaction commit

    }
}
