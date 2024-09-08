package com.example.springpractice.controller;

import com.example.springpractice.entity.Member;
import com.example.springpractice.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember1(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();// 바로 get 지양
        return member.getUsername();
    }

    /** 도메인 클래스 컨버터
     * HTTP 파라미터로 넘어온 엔티티의 pk값으로 엔티티 객체를 찾아서 바인딩
     * 스프링이 중간에서 컨버팅하는 과정을 다 끝내고, Member를 바로 파라미터 결과로 인젝션
     *
     * 주의: 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야함
     * (트랜 잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영 X
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("멤버1"));
    }
}
