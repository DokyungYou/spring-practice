package com.example.springpractice.discount;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mainDiscountPolicy")
public class FixedDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;
    
    // 회원등급이 vip 면 1000원 할인
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return discountFixAmount;
        }else {
            return 0;
        }

    }
}
