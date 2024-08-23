package com.example.springpractice.repository.v2;

import com.example.springpractice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {

}
