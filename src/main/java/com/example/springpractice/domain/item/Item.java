package com.example.springpractice.domain.item;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Data
public class Item {

    // add는 null 허용, edit는 notNull
    @NotNull(groups = UpdateCheck.class)
    private Long id;

    @NotBlank(groups= {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups= {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000 , max = 1000000, groups= {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups= {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1 , max = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
