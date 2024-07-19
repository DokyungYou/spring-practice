package com.example.springpractice;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.order.Order;
import com.example.springpractice.order.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        memberService.signup(new Member(1L, "회원1", Grade.VIP));

        Order order = orderService.createOrder(1L, "농축에너지음료", 100000);
        System.out.println("order:" + order);
        System.out.println("order.calculatePrice(): " + order.calculatePrice());
    }
}
