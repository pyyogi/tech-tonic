package com.kursach.repository;

import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.OrderItem;
import com.kursach.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByUser(User user);

}
