package com.example.springpractice.repository;

import com.example.springpractice.domain.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order){
        entityManager.persist(order);
    }

    public Order findOne(Long id){
       return entityManager.find(Order.class, id);
    }

    // TODO
    //public List<Order> findAll(OrderSearch orderSearch){}
}
