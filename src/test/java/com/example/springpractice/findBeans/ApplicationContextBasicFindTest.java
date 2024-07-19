package com.example.springpractice.findBeans;

import com.example.springpractice.AppConfig;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("bean 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }
    
    @Test
    @DisplayName("bean 이름으로 조회실패")
    void findBeanByName_fail(){

        // assertThrows(Class<T> expectedType, Executable executable)
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("!!!!", MemberService.class) );
    }
    

    @Test
    @DisplayName("타입으로 bean 조회")
    void findBeanByType1(){
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체적인 타입으로 bean 조회")
    void findBeanByType2(){
        // 스프링 빈에 등록된 인스턴스 타입을 보고 결정
        MemberServiceImpl memberService = ac.getBean(MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }


}
