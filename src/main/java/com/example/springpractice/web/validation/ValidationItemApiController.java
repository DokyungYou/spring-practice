package com.example.springpractice.web.validation;

import com.example.springpractice.web.validation.form.ItemSaveForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    /**
     * 각각의 필드 단위로 적용되는 ModelAttribute 와는 다르게
     * HttpMessageConverter 는 전체 객체 단위로 적용
     * 컨버의 작동이 성공하여 객체 생성에 성공해야 검증(@Validate,@ Valid)이 적용
     * 객체 생성 실패시 예외발생
     */
    @PostMapping("/add")
    public Object addItem(@RequestBody @Valid ItemSaveForm form, BindingResult bindingResult){
        log.info("API 컨트롤러 호출");

        if(bindingResult.hasErrors()){
            log.info("검증 오류 발생 errors ={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
