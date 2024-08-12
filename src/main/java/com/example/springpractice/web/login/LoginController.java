package com.example.springpractice.web.login;

import com.example.springpractice.domain.login.LoginService;
import com.example.springpractice.domain.member.Member;
import com.example.springpractice.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
       return "login/loginForm";
    }

    /** 로그인 (쿠키) */
    //@PostMapping("/login")
    public String loginV1(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 불일치"); // global 오류
            return "login/loginForm";
        }

        // 로그인 성공
        // 쿠키에 만료시간 설정하지 않으면 세션쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }


    /** 로그인 (쿠키 + 세션) */
    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 불일치"); // global 오류
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션 생성 -> 회원 데이터 보관
        // 쿠키 생성
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response){
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    /** 로그아웃 (쿠키 + 세션) */
    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request, HttpServletResponse response){
        sessionManager.expire(request);
        expireCookie(response, SessionManager.SESSION_COOKIE_NAME);
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);

        // 세션 쿠키는 브라우저를 닫아야 날라가는데, 현재는 브라우저를 닫지 않고 날리기 위해서 영속쿠키로 덮어씌움
        // 웹브라우저 입장에서 0이기때문에 바로 종료
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


}
