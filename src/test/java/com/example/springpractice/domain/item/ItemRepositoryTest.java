package com.example.springpractice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {


    private ItemRepository itemRepository = new ItemRepository();


    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given (parameter)
        Item item = new Item("상품1", 1000, 1);

        //when (method)
        Item savedItem = itemRepository.save(item);

        //then (assert)
        Item findedItem = itemRepository.findById(savedItem.getId());

        assertThat(savedItem).isEqualTo(findedItem);
        assertThat(savedItem.getId()).isEqualTo(findedItem.getId());
        assertThat(savedItem.getName()).isEqualTo(findedItem.getName());
        assertThat(savedItem.getQuantity()).isEqualTo(findedItem.getQuantity());

    }


    @Test
    void findAll() {
        //given (parameter)
        Item item1 = new Item("상품1", 1000, 1);
        Item item2 = new Item("상품2", 2000, 2);
        Item item3 = new Item("상품3", 3000, 3);

        itemRepository.save(item1); //id 1
        itemRepository.save(item2); //id 2
        itemRepository.save(item3); //id 3

        //when (method)
        List<Item> items = itemRepository.findAll();

        //then (assert)
        assertThat(items.size()).isEqualTo(3);
        assertThat(items).contains(item1, item2, item3); // public final SELF contains(ELEMENT... values)

    }

    @Test
    void updateItem() {
        //given (parameter)
        Item item1 = new Item("상품1", 1000, 1);
        Item item2 = new Item("상품2", 2000, 2);
        Item item3 = new Item("상품3", 3000, 3);

        Item savedItem1 = itemRepository.save(item1);//id 1
        Item savedItem2 = itemRepository.save(item2); //id 2
        Item savedItem3 = itemRepository.save(item3); //id 3

        Item update1 = new Item("item1", 10000, 10);
        Item update2 = new Item("item2", 20000, 20);
        Item update3 = new Item("item3", 30000, 30);

        //when (method)
        itemRepository.updateItem(savedItem1.getId(), update1);
        itemRepository.updateItem(savedItem2.getId(), update2);
        itemRepository.updateItem(savedItem3.getId(), update3);

        //then (assert)
        Item findItem1 = itemRepository.findById(savedItem1.getId());
        Item findItem2 = itemRepository.findById(savedItem2.getId());
        Item findItem3 = itemRepository.findById(savedItem3.getId());

        assertThat(findItem1.getId()).isEqualTo(savedItem1.getId());
        assertThat(findItem2.getId()).isEqualTo(savedItem2.getId());
        assertThat(findItem3.getId()).isEqualTo(savedItem3.getId());

        assertThat(findItem1.getName()).isEqualTo(update1.getName());
        assertThat(findItem2.getName()).isEqualTo(update2.getName());
        assertThat(findItem3.getName()).isEqualTo(update3.getName());

        assertThat(findItem1.getPrice()).isEqualTo(update1.getPrice());
        assertThat(findItem2.getPrice()).isEqualTo(update2.getPrice());
        assertThat(findItem3.getPrice()).isEqualTo(update3.getPrice());

        assertThat(findItem1.getQuantity()).isEqualTo(update1.getQuantity());
        assertThat(findItem2.getQuantity()).isEqualTo(update2.getQuantity());
        assertThat(findItem3.getQuantity()).isEqualTo(update3.getQuantity());

    }

}