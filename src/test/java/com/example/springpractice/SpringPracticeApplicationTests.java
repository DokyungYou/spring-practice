package com.example.springpractice;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringPracticeApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
    }

}
