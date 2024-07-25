package com.example.springpractice.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleWithPrototypeTest1 {
    private ApplicationContext ac;

    @BeforeEach
    void init(){
        ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class);
    }

    @Test
    void findPrototype(){

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype(){

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);

    }

    // 스코프 생략가능 (싱글톤이 디폴트)
    @RequiredArgsConstructor
    static class ClientBean {
        private final PrototypeBean prototypeBean; // 1. 싱글톤빈 생성시에 주입

        private int logic(){ // 2. 프로토타입으로 만들었었으나, 싱글톤 bean 생성 시에 주입된 참조값으로만 로직이 돌아가기때문에, 프로토타입이라는 의도가 무시됨

            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    // 프로토타입의 의도는 주입 받는 시점에 각각 새로운 빈이 생성되는 것 (사용할때마다 새로 생성되는 것이 아님)
    // 싱글톤빈1, 싱글톤빈2 가 각각 의존관계 주입 시 각각 다른 인스턴스를 주입 받게 끔 하는 것
    @Scope("prototype") 
    static class PrototypeBean {
        private int count = 0;

        public void addCount(){
            count++;
        }
        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean init(): " + this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("예의상 만들어봤다.");
        }


    }
}
