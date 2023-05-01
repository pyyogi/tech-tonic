package com.kursach.repository;

import com.kursach.entity.Order;
import com.kursach.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findOrderItemsByOrder(Order order);
}
