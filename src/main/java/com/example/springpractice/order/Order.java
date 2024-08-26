package com.example.springpractice.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders") // db의 예약어인 order by 때문에 orders로 바꿈
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String userName; // 정상, 예외, 잔고부족
    private String payStatus; // 대기, 완료
}
