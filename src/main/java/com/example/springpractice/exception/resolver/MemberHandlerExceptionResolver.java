package com.example.springpractice.exception.resolver;

import com.example.springpractice.exception.custom.MemberException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MemberHandlerExceptionResolver implements HandlerExceptionResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 서블릿 컨테이너까지 예외를 전달하지 않게끔
        // sendError 를 안하고 여기서 처리 (다시 내부 요청 안하게끔)
        try{
            if(ex instanceof MemberException){
                log.info("MemberException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if(acceptHeader.equals("application/json")){
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex",ex.getClass());
                    errorResult.put("message",ex.getMessage());

                    // 서블릿 컨테이너까지 response가 그대로 전달 될 것임
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(errorResult));
                    return new ModelAndView();

                }else{ // accept가 json 외의 타입이 내려올때
                    return new ModelAndView("error/500");
                }
            }
        }catch (IOException e){
            log.error("resolver ex" , e);
        }
        return null;
    }
}
