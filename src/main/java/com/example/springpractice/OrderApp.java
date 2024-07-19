package com.example.springpractice;

import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import com.example.springpractice.order.Order;
import com.example.springpractice.order.OrderService;
import com.example.springpractice.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {

        MemberService memberService = new MemberServiceImpl(new MemoryMemberRepository());
        OrderService orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixedDiscountPolicy());

        memberService.signup(new Member(1L, "회원1", Grade.VIP));

        Order order = orderService.createOrder(1L, "농축에너지음료", 100000);
        System.out.println("order:" + order);
        System.out.println("order.calculatePrice(): " + order.calculatePrice());
    }
}
