package com.example.springpractice.web.filter;

import com.example.springpractice.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 인증 필터를 적용해도 홈, 회원가입, 로그인화면, css 같은 리소스에는 접근 가능해야함
    private static final String[] whiteList = {"/", "/members/add", "/login","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작 {}", requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpServletRequest.getSession(false);// false 안 넣어주면 세션이 없을때 새로운 세션을 만들어버림

                // 세션자체가 없거나, 로그인관련한 속성이 존재하지 않을 때
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청 {}", requestURI);

                    // 로그인페이지로 쫓아낸다.
                    // 로그인 후에 다시 요청하던 url 로 이동하게끔 한다.
                    // login 버전4
                    httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; // 해당 요청의 다음 서블릿이나, 컨트롤러 호출 안하고 끝냄
                }
            }
            chain.doFilter(request,response);

        }catch (Exception e){
            throw e;
            // 예외 로깅가능하지만, 톰캣까지 예외를 보내주어야한다.
            // 서블릿 필터에서 예외가 발생했을 때 여기서 먹어버리면 정상 흐름처럼 동작해버리기 때문에 서블릿 컨테이너, WAS 까지 올려줘야한다?
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트 url 요청일 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}
