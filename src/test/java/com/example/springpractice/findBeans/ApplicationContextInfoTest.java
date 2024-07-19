package com.example.springpractice.findBeans;

import com.example.springpractice.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("스프링에 등록된 모든 bean 출력")
    void printAllBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // bean으로 정의된 이름들 꺼내기

        System.out.println("==== All Beans ====");
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName); // 타입 지정안해서 Object반환
            System.out.println("name: " + beanDefinitionName + " |  object: " + bean);
        }
    }



    @Test
    @DisplayName("애플리케이션 bean 출력")  // 내부에서 뭔가를 하기위해 등록된 bean이 아닌 주로 애플리케이션개발을 위해 등록한 bean들 (+ 외부 라이브러리 등)
    void printApplicationBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // bean으로 정의된 이름들 꺼내기

        System.out.println("==== Application Beans ====");
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                Object bean = ac.getBean(beanDefinitionName); // 타입 지정안해서 Object반환
                System.out.println("name: " + beanDefinitionName + " |  object: " + bean);
            }
        }
    }


    @Test
    @DisplayName("내부에서 사용하는 bean 출력")
    void printInfrastructureBean(){

        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // bean으로 정의된 이름들 꺼내기

        System.out.println("==== Infrastructure Beans ====");
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE){
                Object bean = ac.getBean(beanDefinitionName); // 타입 지정안해서 Object반환
                System.out.println("name: " + beanDefinitionName + " |  object: " + bean);
            }
        }
    }
    

}
