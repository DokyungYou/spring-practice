package com.example.springpractice.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 컨트롤러에서 발생한 예외를 여기서 꿀꺽 먹어버림
            try{
                if(ex instanceof IllegalArgumentException){
                    log.info("IllegalArgumentException resolver to 400");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage()); // 실제 에러였던걸 sendError 로 바꿔치기

                    // 아무것도 없이 빈 값으로 넘기면 정상적인 흐름으로 계속 반환되면서 WAS까지 정상적 흐름으로 이어짐 (그리고 다시 오류처리 내부 요청로직을 탐)
                    // ModelAndView 지정 시 뷰 렌더링
                    return new ModelAndView(); 
                }
            } catch (IOException e) {
                log.error("resolver ex", e);
            }
            return null; // null로 반환 시 예외가 WAS까지 날아감
        }

}
