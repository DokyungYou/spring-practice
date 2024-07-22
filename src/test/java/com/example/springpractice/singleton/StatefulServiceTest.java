package com.example.springpractice.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulServiceTest {

    @Test
    @DisplayName("싱글톤객체 - 상태유지 필드")
    void statefulServiceSingleton(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA에서 회원1이 200_000원 주문
        statefulService1.order("회원1", 200_000);

        // ThreadA에서 회원2이 500_000원 주문
        statefulService1.order("회원2", 500_000);

        assertThat(statefulService1.getPrice()).isNotSameAs(200_000);

    }

    static class TestConfig{

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}