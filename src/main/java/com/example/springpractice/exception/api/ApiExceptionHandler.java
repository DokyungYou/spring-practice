package com.example.springpractice.exception.api;

import com.example.springpractice.exception.custom.BadRequestException;
import com.example.springpractice.exception.custom.MemberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiExceptionHandler {

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값입니다!"); // 본래 500으로 전달될 것을 400으로 바꿀 것이다.
        }
        if(id.equals("member-ex")){
            throw new MemberException("사용자 오류");
        }
        return new MemberDto(id, "이름");
    }

    @GetMapping("/response-status-ex1")
    public String responseStatusEx1(){
        throw new BadRequestException();
    }

    @GetMapping("/response-status-ex2")
    public String responseStatusEx2(){
        // 상태코드와 오류 메시지까지 한번에 해결할 수 있는 특수한 예외클래스
        // 얘도 ResponseStatusExceptionResolver 가 처리해줌
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/default-handler-ex")
    public String defaultException(@RequestParam Integer data){
        return "ok";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
