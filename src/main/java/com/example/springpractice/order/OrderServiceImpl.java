package com.example.springpractice.order;

import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

//    @Autowired
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//        this.memberRepository = memberRepository;
//    }

    // 수정자 주입 테스트용
/*    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy = discountPolicy;
    }*/


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);// 단일책임원칙이 잘 지켜짐

        return new Order(memberId, itemName ,itemPrice, discountPrice);
    }

    // Test용
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
