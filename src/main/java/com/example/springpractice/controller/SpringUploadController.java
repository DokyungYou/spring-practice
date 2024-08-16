package com.example.springpractice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;


    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }


    /**
     * @RequestParam MultipartFile file -> 업로드하는 HTML Form의 이름에 맞추어 @RequestParam을 적용하면 됨
     * @ModelAtrribute 에서도 동일하게 사용 가능
     */
    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file, // <li>파일<input type="file" name="file" ></li>
                           HttpServletRequest request) throws IOException {

        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if(!file.isEmpty()){
            String fullPath = fileDir + file.getOriginalFilename(); // 업로드 파일 명
            log.info("파일 저장 fullPath={}", fullPath);
            file.transferTo(new File(fullPath)); // 파일 저장
        }
        return "upload-form";
    }
}
