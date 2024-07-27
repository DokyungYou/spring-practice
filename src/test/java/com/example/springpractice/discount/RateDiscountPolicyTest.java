package com.example.springpractice.discount;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @DisplayName("VIP는 5% 할인 적용")
    @Test
    void discount_vip() {
        //given (parameter)
        Member member = new Member(1L,"VIP회원" ,Grade.VIP);
        int price = 20000;

        //when (method)
        int vipDiscount = rateDiscountPolicy.discount(member, price);

        //then (assert)
        assertThat(vipDiscount).isEqualTo(1000);
    }

    @DisplayName("일반회원은 할인 적용 X")
    @Test
    void discount_basic() {
        //given (parameter)
        Member member = new Member(1L,"VIP회원" ,Grade.BASIC);
        int price = 20000;

        //when (method)
        int vipDiscount = rateDiscountPolicy.discount(member, price);

        //then (assert)
        assertThat(vipDiscount).isEqualTo(0);
    }
}