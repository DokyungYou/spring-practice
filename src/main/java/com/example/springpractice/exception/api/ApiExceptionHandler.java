package com.example.springpractice.exception.api;

import com.example.springpractice.exception.custom.MemberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Getter
    @Setter
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
