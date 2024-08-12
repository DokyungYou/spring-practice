package com.example.springpractice.web.member;

import com.example.springpractice.domain.member.Member;
import com.example.springpractice.domain.member.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") MemberAddForm form){
        return "/members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("member") MemberAddForm form,
                       BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "members/addMemberForm"; // 잘못 입력했던 member를 model에 담아서 이동
        }

        memberRepository.save(form.toMember());
        return "redirect:/";
        // form 만 이동하면 새로고침 시에 계속 post - "/add" 를 요청하게 된다. (의도치 않게 계속 member 추가)
    }


}
