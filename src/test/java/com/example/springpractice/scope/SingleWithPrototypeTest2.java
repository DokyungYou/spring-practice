package com.example.springpractice.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleWithPrototypeTest2 {
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
        int count1_1 =clientBean1.logic();
        int count1_2 =clientBean1.logic();
        int count1_3 = clientBean1.logic();
        assertThat(count1_1).isEqualTo(1);
        assertThat(count1_2).isEqualTo(1);
        assertThat(count1_3).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2_1 = clientBean2.logic();
        assertThat(count2_1).isEqualTo(1);

    }

    // 스코프 생략가능 (싱글톤이 디폴트)
    @RequiredArgsConstructor
    static class ClientBean {
        private final PrototypeBean prototypeBean;
        private final Provider<PrototypeBean> prototypeBeanProvider; // jakarta.inject.Provider 로 변경
       //private final ObjectFactory<PrototypeBean> prototypeBeanFactory;

        private int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

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
