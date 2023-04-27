package com.kursach.service;

import com.kursach.entity.Order;
import com.kursach.entity.User;
import com.kursach.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService{

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImplementation(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOrderByPerson(User user) {
        return orderRepository.findOrderByUser(user);
    }

    @Override
    public HttpStatus Save(Order order) {
        orderRepository.save(order);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus createOrder(User user) {
        return null;
    }
}
