package com.example.springpractice.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Setter 지양
@DiscriminatorValue("A")
@Entity
public class Album extends Item {

    private String artist;
    private String etc;
}
