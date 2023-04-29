package com.kursach.service;

import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.OrderItem;
import com.kursach.entity.User;
import com.kursach.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Transactional
    public HttpStatus createOrderFromUserDevices(String username) {
        User user = (User) userService.loadUserByUsername(username);
        if (user == null) {
            return HttpStatus.NOT_FOUND;
        }
        Set<Device> devices = user.getDevices();
        if (devices == null || devices.isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        }

        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        int sumPrice = 0;

        for (Device device : devices) {
            OrderItem orderItem = new OrderItem();
            orderItem.setDevice(device);
            orderItem.setQuantity(1);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            sumPrice += device.getPrice();
        }
        order.setOrderItems(orderItems);
        order.setSumPrice(sumPrice);
        orderRepository.save(order);

        user.setDevices(new HashSet<>());
        userService.saveUser(user);

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus changeOrderItemQuantity(Long orderId, Long orderItemId, int quantity) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return HttpStatus.NOT_FOUND;
        }
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        }
        OrderItem orderItem = orderItems.stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElse(null);
        if (orderItem == null) {
            return HttpStatus.NOT_FOUND;
        }
        if (quantity < 1) {
            return HttpStatus.BAD_REQUEST;
        }
        order.setSumPrice(order.getSumPrice()+(orderItem.getQuantity()-quantity)*orderItem.getDevice().getPrice());
        orderItem.setQuantity(quantity);
        orderRepository.save(order);
        return HttpStatus.OK;
    }
}

















//@Service
//public class OrderService {


//    private final OrderRepository orderRepository;
//
//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    public List<Order> getOrdersByUser(User user) {
//        return orderRepository.findByUser(user);
//    }
//
//    public HttpStatus saveOrder(Order order) {
//        orderRepository.save(order);
//        return HttpStatus.CREATED;
//    }
//
//    public Optional<Order> getOrderById(Long id) {
//        return orderRepository.findById(id);
//    }
//
//    public HttpStatus deleteOrder(Order order) {
//        orderRepository.delete(order);
//        return HttpStatus.OK;
//    }
//}
