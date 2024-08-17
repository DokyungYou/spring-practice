package com.example.springpractice.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ItemForm {
    private Long itemId;
    private String itemName; // <li>상품명 <input type="text" name="itemName"></li>
    private MultipartFile attachFile; // <li>첨부파일<input type="file" name="attachFile" ></li>
    private List<MultipartFile> imageFiles; //<li>이미지 파일들<input type="file" multiple="multiple" name="imageFiles" ></li>

}
