package com.example.springpractice.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Item {


    private Long id;
    private String name;

    // price, quantity 를 Integer로 한 이유: 안 들어갈 때도 있다고 가정 (null로 받기위함)
    private Integer price;
    private Integer quantity;

    public Item(String name, Integer price, Integer quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
