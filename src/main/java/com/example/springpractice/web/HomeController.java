package com.example.springpractice.web;

import com.example.springpractice.domain.member.Member;
import com.example.springpractice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    /**
     * 클라이언트로부터 Cookie 가져오는 법
     * - HttpServletRequest 에서 꺼내기
     * - 스프링에서 제공하는 @CookieValue 사용 (자동으로 타입 컨버팅도 해준다) String -> 특정 타입
     * 
     * 로그인안한 사용자를 위해 required = false 로 설정
     */
    @GetMapping("/")
    public String homeLogin(Model model, 
                            @CookieValue(name ="memberId", required = false) Long memberId){

        if(memberId == null){
            return "home";
        }

        // 로그인 성공
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}