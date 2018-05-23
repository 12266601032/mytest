package com.sample.service;

import com.sample.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void save() {
        Order order = new Order();
        order.setUserId(1l);
        order.setOrderId(1l);
        Order order2 = new Order();
        order2.setUserId(2l);
        order2.setOrderId(2l);

        orderService.save(Arrays.asList(order,order2));
    }
}