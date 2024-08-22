package com.example.springpractice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // JPA가 사용하는 객체라는 뜻 (해당 어노테이션이 있어야 JPA가 인식)
@Table(name = "item") // 객체명과 테이블이 이름이 동일하면 생략 가능
public class Item {

    @Id // 테이블의 pk와 해당 필드를 매핑
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk생성 값을 db에서 생성하는 방식을 결정
    private Long id;

    // 객체의 필드를 테이블의 컬럼과 매핑
    // 생략 시 필드의 이름을 테이블 컬럼 이름으로 사용 (스프링부트와 통합 사용 시 객체필드의 카멜 케이스 -> 테이블컬럼의 스네이크 케이스로 자동변환)
    @Column(name = "item_name", length = 10)
    private String itemName;

    private Integer price;

    private Integer quantity;


    // JPA는 public 또는 protected 기본생성자가 필수 (물론 @NoArgsConstructor 로 해줘도 됨)
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
