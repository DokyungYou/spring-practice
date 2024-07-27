package com.example.springpractice.order;

import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceImplTest {

    MemberRepository memberRepository;

    @BeforeEach
    void init(){
        memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "회원1", Grade.BASIC));
    }

    @Test
    void test(){
        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixedDiscountPolicy());
        Order order = orderService.createOrder(1L, "상품1", 100_000);

        assertThat(order.getMemberId()).isEqualTo(memberRepository.findById(1L).getId());
    }
}