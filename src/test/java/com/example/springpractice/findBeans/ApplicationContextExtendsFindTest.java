package com.example.springpractice.findBeans;

import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모타입으로 조회시, 자식이 둘 이상 있으면, 중복 오류발생")
    void findBeanByParentTypeDuplicate(){
        assertThrows(NoUniqueBeanDefinitionException.class, ()-> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모타입으로 조회시, 자식이 둘 이상 있으면, 이름 지정 조회")
    void findBeanByParentTypeBeanName(){
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모타입으로 조회시, 자식이 둘 이상 있으면, 하위타입 지정 조회") // 지양해야함
    void findBeanBySubType(){
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", RateDiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모타입으로 모두 조회")
    void findAllBeansByParentType(){
        Map<String, DiscountPolicy> beans = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beans.size()).isEqualTo(2);
//        for (String key : beans.keySet()) {
//            System.out.println("key: " + key + "  | value: " + beans.get(key));
//        }
    }

    @Test
    @DisplayName("Object 타입으로 모두 조회")
    void findAllBeansByObjectType(){
        Map<String, Object> beans = ac.getBeansOfType(Object.class);
        for (String key : beans.keySet()) {
            System.out.println("key: " + key + "  | value: " + beans.get(key));
        }
    }
    

    @Configuration
    static class TestConfig{

        @Bean
        public DiscountPolicy rateDiscountPolicy(){
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixedDiscountPolicy(){
            return new FixedDiscountPolicy();
        }

    }
}
