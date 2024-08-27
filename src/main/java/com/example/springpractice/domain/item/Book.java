package com.example.springpractice.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Setter 지양
@DiscriminatorValue("B")
@Entity
public class Book extends Item {

    private String author;
    private boolean isBn;
}
