package com.example.springpractice.order;

import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);// 단일책임원칙이 잘 지켜짐

        return new Order(memberId, itemName ,itemPrice, discountPrice);
    }
}
