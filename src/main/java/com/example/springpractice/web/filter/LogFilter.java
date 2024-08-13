package com.example.springpractice.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        // ServletRequest 는 HTTP 요청이 아닌 경우까지 고려해서 만든 인터페이스
        // 기능이 별로 없어서 다운캐스팅해서 사용
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();

        // 사용자 구분하는 용으로 넣었음
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}][{}]", uuid, requestURI);

            // 다음필터로 넘어가줘야한다. (doFilter 호출을 안하면 요청 과정이 여기에서끝나버림, 서블릿호출못하고 컨트롤러까지 진입 못함)
            // 등록한 필터가 하나여도 무조건 호출해줘야한다.(다음 필터가 없으면 서블릿 호출)
            chain.doFilter(request, response);

        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }
    
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
