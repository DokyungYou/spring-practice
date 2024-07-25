package com.example.springpractice.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonTest {

    @Test
    void findSingletonBean(){

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        Assertions.assertThat(singletonBean1).isEqualTo(singletonBean2);

        ac.close();
    }
    @Scope("singleton") // 싱글톤은 생략가능
    static class SingletonBean {

        @PostConstruct
        public void init(){
            System.out.println("SingletonBean init() 호출");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean destroy() 호출");
        }
    }
}
