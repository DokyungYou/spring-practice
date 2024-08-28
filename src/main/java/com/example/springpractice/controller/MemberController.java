package com.example.springpractice.controller;

import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Member;
import com.example.springpractice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String create(@Valid MemberForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }


    /**
     * ssr 하는 경우에는 어차피 서버 안에서 돌기때문에
     * 템플릿에 엔티티를 그대로 넘겨도 원하는 데이터만 출력하기때문에 큰 문제는 없으나
     *
     * api로 만들 때는 절대로 엔티티를 웹으로 반환하면 안된다. (외부 노출 X)
     * - 중요한 데이터 노출
     * - 엔티티의 구조에 변경이 생기면 api스펙 변동 -> 불안전 API 스펙이 됨
     */
    @GetMapping
    public String list(Model model){

        List<Member> members = memberService.findMembers(); 
        model.addAttribute("members", members); // 엔티티를 그대로 넘기는 것은 지양
        return "members/memberList";
    }
}
