package com.example.springpractice.discount;

import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("rateDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 5;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountPercent / 100;
        }
        return 0;
    }
}
