package com.example.springpractice.web;

import com.example.springpractice.domain.member.Member;
import com.example.springpractice.domain.member.MemberRepository;
import com.example.springpractice.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    /**
     * 클라이언트로부터 Cookie 가져오는 법
     * - HttpServletRequest 에서 꺼내기
     * - 스프링에서 제공하는 @CookieValue 사용 (자동으로 타입 컨버팅도 해준다) String -> 특정 타입
     * 
     * 로그인안한 사용자를 위해 required = false 로 설정
     */
   // @GetMapping("/")
    public String homeLoginV1(Model model,
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

   // @GetMapping("/")
    public String homeLoginV2(Model model, HttpServletRequest request){
        
        // 세션에 저장된 회원정보 조회
        Member member = (Member) sessionManager.getSession(request);

        // 로그인 성공
        if(member == null){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(Model model, HttpServletRequest request){

        // 세션에 저장된 회원정보 조회
        HttpSession session = request.getSession(false);
        log.info("session={}", session);

        // 세션에 회원 데이터가 없다면 기본 home
        if(session == null){
            model.addAttribute("member", new Member());
            return "home";
        }

        // 세션이 유지되면 로그인Home 으로 이동
        log.info("왜 null이 아닌가??");
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("member = {}", loginMember);

        model.addAttribute("member", loginMember);
        return "loginHome";
    }


    /** @SessionAttribute
     * 해당 기능은 세션을 생성하지 않음
     */
    @GetMapping("/")
    public String homeLoginV3Spring(
            Model model,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){


        // 세션에 회원 데이터가 없다면 기본 home
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되면 로그인Home 으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}