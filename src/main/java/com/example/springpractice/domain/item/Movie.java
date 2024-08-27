package com.example.springpractice.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Setter 지양
@DiscriminatorValue("M")
@Entity
public class Movie extends Item {

    private String director;
    private String actor;
}
