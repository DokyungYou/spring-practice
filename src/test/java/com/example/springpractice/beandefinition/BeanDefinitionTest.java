package com.example.springpractice.beandefinition;

import com.example.springpractice.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig .class);

    @Test
    @DisplayName("Bean 설정 메타정보 확인")
    void findApplicationBean(){
        for (String beanDefinitionName : ac.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                System.out.println(
                        "beanDefinitionName: " + beanDefinitionName + " | " +
                        "beanDefinition: " + beanDefinition
                        );
            }
        }
    }

}
