package com.example.springpractice.domain.item;

import lombok.Getter;

@Getter
public enum ItemType {
    
    BOOK("도서"),
    FOOD("식품"),
    ETC("기타");
    private final String description;

    ItemType(String description){ // enum의 생성자 접근제어자는 private
        this.description = description;
    }
}
