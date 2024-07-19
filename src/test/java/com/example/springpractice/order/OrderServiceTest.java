package com.example.springpractice.order;

import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrderServiceTest {


    MemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService = new MemberServiceImpl(memberRepository);
    OrderService orderService = new OrderServiceImpl(memberRepository, new FixedDiscountPolicy());

    @DisplayName("주문생성 - 성공")
    @Test
    void createOrder() {
        //given (parameter)
        Member member = new Member(1L, "회원1", Grade.VIP);
        memberService.signup(member);

        //when (method)
        Order order = orderService.createOrder(member.getId(), "우롱차", 200000);

        //then (assert)
        assertThat(order.getDiscountPrice()).isEqualTo(1000); // 정액할인제 검사
    }
}