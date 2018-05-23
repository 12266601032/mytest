package com.sample.service;

import com.sample.entity.Order;
import com.sample.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void save(List<Order> orderList){
        orderRepository.save(orderList);
    }

}
