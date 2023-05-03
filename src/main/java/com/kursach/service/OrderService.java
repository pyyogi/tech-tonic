package com.kursach.service;

import com.kursach.dto.ItemQuantityDto;
import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.OrderItem;
import com.kursach.entity.User;
import com.kursach.repository.DeviceRepository;
import com.kursach.repository.OrderItemRepository;
import com.kursach.repository.OrderRepository;
import com.kursach.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<Optional<Order>> getOrderById(Long orderId, Principal principal) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && order.get().getUser().getUsername().equals(principal.getName())) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Transactional
    public ResponseEntity<Optional<List<Order>>> getOrders(String username, Principal principal) {
        User user = userRepository.findByUsername(username);
        Optional<List<Order>> orders = orderRepository.findByUser(user);
        if (username.equals(principal.getName())) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Transactional
    public HttpStatus createOrder(String username, List<ItemQuantityDto> items, Principal principal) {
        // Find the user by username
        User user = userRepository.findByUsername(username);
        if (user == null || !principal.getName().equals(username)) {
            return HttpStatus.BAD_REQUEST;
        }
        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());
        orderRepository.save(order);
        // Iterate through the device quantities and create order items for each one
        for (ItemQuantityDto item :
                items) {

            Device device = deviceService.getById(item.getDeviceId());
            if (device != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setDevice(device);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrder(order);
                orderItemRepository.save(orderItem);
                order.getOrderItems().add(orderItem);
            }

        }

        // Calculate the sum price
        int sumPrice = calculateSumPrice(order.getId());
        order.setSumPrice(sumPrice);

        // Save the order
        orderRepository.save(order);

        // Clear the user's cart
        user.getDevices().clear();
        userRepository.save(user);
        return HttpStatus.OK;


    }

    public int calculateSumPrice(Long orderId) {
        int sum = 0;
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrder(orderRepository.getById(orderId));
        for (OrderItem orderItem : orderItems) {
            sum += orderItem.getQuantity() * orderItem.getDevice().getPrice();
        }
        return sum;
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
