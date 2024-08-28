package com.example.springpractice.domain.item;

import com.example.springpractice.domain.Category;
import com.example.springpractice.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.InheritanceType.SINGLE_TABLE;


@Getter @Setter //Setter 지양
@Inheritance(strategy = SINGLE_TABLE) // 한 테이블에 다 몰아넣는다.
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    /** 도메인주도 설계
     *  - 엔티티 자체가 해결할 수 있는 것들은 엔티티 안에 비즈니스 로직을 넣자
     *  - 객체지향적
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
