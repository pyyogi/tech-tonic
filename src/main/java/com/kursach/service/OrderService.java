package com.kursach.service;

import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.User;
import com.kursach.repository.OrderRepository;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order save(Order order);
    List<Order> findOrderByPerson(User user);

    public HttpStatus Save(Order order);

    public HttpStatus createOrder(User user);
}
