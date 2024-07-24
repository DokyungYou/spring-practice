package com.example.springpractice.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close(); // 컨테이너 종료 (빈들도 같이 종료)
    }

    @Configuration
    static class LifeCycleConfig {

        // 빈으로 등록시에 해당 콜백 함수들 발생
        // destroyMethod의 기본값은 (inferred)으로 등록 ->  close, shutdown 이라는 이름의 메서드 자동 호출 (라이브러리의 종료 메서드를 추론해서 호출)
        @Bean(initMethod = "init", destroyMethod = "close") // 직접 종료메서드를 설정하지 않아도 추론기능에 의해 자동호출 (사용하지 않으려면 "" 공백으로 설정)
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://great-spring.dev");
            return networkClient;
        }
    }
}
