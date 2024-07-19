package com.example.springpractice;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.service.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {

        // 스프링은 모든 것이 ApplicationContext 로 시작 (스프링 컨테이너라고 보면 된다)
        // @Configuration이 붙은 AppConfig.class 를 설정 정보로 사용
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member1 = new Member(1L, "회원1", Grade.BASIC);
        memberService.signup(member1);

        Member findedMember = memberService.findMember(1L);
        System.out.println(member1);
        System.out.println(findedMember);

        System.out.println(member1.equals(findedMember));
    }
}
