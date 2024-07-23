package com.example.springpractice.order;

import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // @Autowired 필드명 방식
/*    @Autowired  // DiscountPolicy <- RateDiscountPolicy, FixedDiscountPolicy
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy fixedDiscountPolicy) { // 동일 타입의 bean이 여러개일 때, bean이름과 파라미터 이름으로 매칭
        this.discountPolicy = fixedDiscountPolicy;
        this.memberRepository = memberRepository;
    }*/

    // @Qualifier 방식 (생성자 외 수정자, 필드, 수동 bean 등록 시에도 사용 가능)
    // 빈 이름 변경 X, 추가 구분자역할
//    @Autowired  // DiscountPolicy <- RateDiscountPolicy, FixedDiscountPolicy
//    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy fixedDiscountPolicy) { // 같은 Qualifier를 찾아서 주입
//        this.discountPolicy = fixedDiscountPolicy;
//        this.memberRepository = memberRepository;
//    }


    // @Primary 방식 (동일 타입의 bean이 여러개일 때, @Primary 인 bean 주입)
    // 메인으로 사용할 bean은 @Primary, 서브로 사용할 bean은 @Qualifier방식으로로 두 방식을 같이 쓰면 된다.
    @Autowired  // DiscountPolicy <- RateDiscountPolicy, FixedDiscountPolicy
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) { 
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }


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
