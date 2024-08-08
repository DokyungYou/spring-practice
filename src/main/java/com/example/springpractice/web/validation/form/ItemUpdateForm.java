package com.example.springpractice.web.validation.form;

import com.example.springpractice.domain.item.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000 , max = 1000000)
    private Integer price;

    @NotNull
    @Range(min = 1 , max = 9999)
    private Integer quantity;

    public Item toItem(){
        Item item = new Item();
        item.setId(id);
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        return item;
    }
}
