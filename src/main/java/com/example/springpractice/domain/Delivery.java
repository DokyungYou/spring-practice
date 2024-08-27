package com.example.springpractice.domain;

import com.example.springpractice.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Setter 지양
@Entity
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //@JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
