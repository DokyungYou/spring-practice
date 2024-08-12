package com.example.springpractice;

import com.example.springpractice.domain.item.Item;
import com.example.springpractice.domain.item.ItemRepository;
import com.example.springpractice.domain.member.Member;
import com.example.springpractice.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @PostConstruct // 빈 생성된 후 초기화 작업을 수행 (복습 시 빈 생명주기 부분 참고)
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test!");
        member.setName("테스터");
        memberRepository.save(member);
    }

}