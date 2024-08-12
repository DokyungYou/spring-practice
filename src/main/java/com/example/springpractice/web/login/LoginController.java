package com.example.springpractice.web.login;

import com.example.springpractice.domain.login.LoginService;
import com.example.springpractice.domain.member.Member;
import com.example.springpractice.web.SessionConst;
import com.example.springpractice.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    //@PostMapping("/login")
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

    /** HttpSession 사용
     *
     * 서블릿이 제공하는 HttpSession 도 결국 앞서 직접 만들어본 SessionManager 와 같은 방식으로 동작
     * 서블릿을 통해 HttpSession 을 생성하면 다음과 같은 쿠키를 생성한다. 쿠키 이름이 JSESSIONID 이고, 값은 추정
     * 불가능한 랜덤 값이다.
     * Cookie: JSESSIONID=5B78E23B513F50164D6FDD8C97B0AD05
     */
    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute("loginForm") LoginForm form,
                          BindingResult bindingResult,
                          HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 불일치"); // global 오류
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // getSession()에 true로 넣어주면 세션이 있을때는 있는 기존의 세션 반환, 없다면 신규 세션 생성
        // 사실 기본값이 true 이기 때문에 생략 가능
        HttpSession session = request.getSession(true);
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response){
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    /** 로그아웃 (쿠키 + 세션) */
    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request, HttpServletResponse response){
        sessionManager.expire(request);
        expireCookie(response, SessionManager.SESSION_COOKIE_NAME);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false); // 기존 세션이 없다면 null반환
        if(session != null){
            expireCookie(response, "JSESSIONID"); // 남아있어도 상관없지만 지웠음
            session.invalidate(); // 해당 세션과 안에 있는 데이터가 다 날라감
        }


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
