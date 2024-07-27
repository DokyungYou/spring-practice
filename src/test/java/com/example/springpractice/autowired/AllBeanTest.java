package com.example.springpractice.autowired;

import com.example.springpractice.AutoAppConfig;
import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.member.Grade;
import com.example.springpractice.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AllBeanTest {

    @Test
    void findAllBeans(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "회원1", Grade.VIP);

        int fixedDiscountPrice = discountService.discount(member, 100_000, "fixedDiscountPolicy");
        int rateDiscountPrice = discountService.discount(member, 100_000, "rateDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(fixedDiscountPrice).isEqualTo(1000);

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(rateDiscountPrice).isEqualTo(5000);
    }

    static class DiscountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policyList;

        // @Autowired 생략가능
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) { // 리스트로도 받을 수 있음
            this.policyMap = policyMap;
            this.policyList = policyList;

            System.out.println("policyMap: " + policyMap);
            System.out.println("policyList: " + policyList);

            /*
            policyMap: {fixedDiscountPolicy=com.example.springpractice.discount.FixedDiscountPolicy@4cee7fa0, rateDiscountPolicy=com.example.springpractice.discount.RateDiscountPolicy@7a26928a}
            policyList: [com.example.springpractice.discount.FixedDiscountPolicy@4cee7fa0, com.example.springpractice.discount.RateDiscountPolicy@7a26928a]
            */
        }

        public int discount(Member member, int price, String discountPolicyKey) { // 실제로는 안전하게 enum 활용하면 좋을듯
            DiscountPolicy discountPolicy = policyMap.get(discountPolicyKey);
            return discountPolicy.discount(member, price);
        }
    }
}
