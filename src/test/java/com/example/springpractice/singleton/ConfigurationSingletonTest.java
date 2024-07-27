package com.example.springpractice.singleton;

import com.example.springpractice.AppConfig;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.service.MemberServiceImpl;
import com.example.springpractice.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSingletonTest {

    @Test
    void test(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);

        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        assertThat(memberRepository).isEqualTo(memberRepository1);
        assertThat(memberRepository).isEqualTo(memberRepository2);
        assertThat(memberRepository1).isEqualTo(memberRepository2);
    }
    @Test
    void configurationDeep(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class); // 얘도 bean

        System.out.println("bean: " + bean.getClass());
        // bean: class com.example.springpractice.AppConfig$$SpringCGLIB$$0
        /*
        @Configuration 이 붙은 클래스를 설정클래스로 넣을 경우
        해당 클래스 자체가 bean으로 등록되는 것이 아닌,
        스프링이 해당 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 클래스를 빈으로 등록한다. (해당 bean이 싱글톤이 보장되게끔 해줌)
        */
    }
}
