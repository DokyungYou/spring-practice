package com.example.springpractice.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mv = new ModelAndView("/response/hello")
                .addObject("data", "hello hello!");
        return mv;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello hello!!!!");
        return "response/hello"; // 해당 문자열로 viewResolver 가 실행돼서 view를 찾고 렌더링
    }


    // 명시성이 떨어져서 권장 X
    @RequestMapping("/response/hello") // 요청url을 논리적 view의 이름으로 진행
    public void responseViewV3(Model model){
        model.addAttribute("data", "!!!hello hello!!!!");
    }
}
