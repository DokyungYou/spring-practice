package com.example.springpractice.domain.item;

import com.example.springpractice.domain.Category;
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

}
