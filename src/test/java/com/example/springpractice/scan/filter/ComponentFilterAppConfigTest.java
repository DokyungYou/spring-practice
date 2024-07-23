package com.example.springpractice.scan.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        assertThrows(
                NoSuchBeanDefinitionException.class,
                ()-> ac.getBean("beanB", BeanB.class)) ;

    }


    // 이렇게 옵션을 변경하면서 사용하기보다는 스프링 기본 설정에 최대한 맞추는 것을 권장
    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(
                    type = FilterType.ANNOTATION, // 필터의 타입 디폴트는 FilterType.ANNOTATION 이기 때문에 생략가능
                    classes = MyIncludeComponent.class
            ),
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.ANNOTATION,
                    classes = MyExcludeComponent.class
            )
    )
    static class ComponentFilterAppConfig{

    }
}
