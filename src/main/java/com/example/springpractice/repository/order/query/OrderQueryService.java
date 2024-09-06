package com.example.springpractice.repository.order.query;

import com.example.springpractice.api.OrderApiController;
import com.example.springpractice.api.OrderApiController.OrderDto;
import com.example.springpractice.domain.Order;
import com.example.springpractice.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersOsivOff(int offset, int limit){

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> collect = orders.stream()
                //.map(order -> new OrderDto(order))
                .map(OrderDto::new)
                .collect(toList());


        return collect;
    }


}
