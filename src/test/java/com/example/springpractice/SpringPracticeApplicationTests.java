package com.example.springpractice;

import com.example.springpractice.entity.Hello;
import com.example.springpractice.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class SpringPracticeApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        entityManager.persist(hello);

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QHello qHello = new QHello("h"); // alias

        Hello findHello = queryFactory
                .selectFrom(qHello)
                .fetchOne();

        assertThat(findHello).isEqualTo(hello);
    }

}
