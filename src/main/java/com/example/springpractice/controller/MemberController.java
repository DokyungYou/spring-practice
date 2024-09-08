package com.example.springpractice.controller;

import com.example.springpractice.dto.MemberDto;
import com.example.springpractice.entity.Member;
import com.example.springpractice.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    /**
     * Pageable 은 인터페이스, 실제는 구현체인 org.springframework.data.domain.PageRequest 객체 생성 후 인젝션
     *
     * ex) /members?page=0&size=3&sort=id,desc&sort=username,desc
     * page: 현재 페이지, 0부터 시작한다.
     * size: 한 페이지에 노출할 데이터 건수
     * sort: 정렬 조건을 정의한다. 예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort 파라 미터 추가 ( asc 생략 가능)
     *
     * 디폴트 설정
     * - 글로벌 설정
     * - @PageableDefault
     */
    @GetMapping("/members1")
    public Page<Member> list1(@PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC)
                                 Pageable pageable){
        // finalAll 과 같은 기본메서드에 Pageable 을 파라미터로 넘길 수 있음
        return memberRepository.findAll(pageable);
    }

    /**
     * 페이징 정보가 둘 이상이면 접두사로 구분
     * @Qualifier 에 접두사명 추가 "{접두사명}_xxx" 예제: /members?member_page=0&order_page=1
     */
    @GetMapping("/members2")
    public Page<Member> list2(@Qualifier("member") Pageable memberPageable,
                              @Qualifier("team") Pageable teamPageable){
        // finalAll 과 같은 기본메서드에 Pageable 을 파라미터로 넘길 수 있음
        return memberRepository.findAll(memberPageable);
    }

    @GetMapping("/members3")
    public Page<MemberDto> list3(@PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC)
                              Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto::new);
    }


    @PostConstruct
    public void init(){
        for (int i = 1; i < 100; i++) {
            memberRepository.save(new Member("멤버" + i, i));
        }
    }
}
