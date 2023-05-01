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

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

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
//        order.setStatus("IN PROCESS");
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

    @Transactional
    public HttpStatus confirmOrder(Long orderId){
        Order order = orderRepository.getById(orderId);
        if (order == null){
            return HttpStatus.NOT_FOUND;
        }
//        order.setStatus("ORDERED");
        return HttpStatus.OK;
    }


    @Transactional
    public void createOrder(User user, List<ItemQuantityDto> items) {
        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());
        orderRepository.save(order);
        // Iterate through the device quantities and create order items for each one
        for (ItemQuantityDto item:
            items) {

                Device device = deviceService.getById(item.getDeviceId());
                if ( device != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setDevice(device);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setOrder(order);
                    orderItemRepository.save(orderItem);
                    order.getOrderItems().add(orderItem);
                }

        }

        // Calculate the sum price
        int sumPrice = calculateSumPrice(order);
        order.setSumPrice(sumPrice);

        // Save the order
        orderRepository.save(order);

        // Clear the user's cart
        user.getDevices().clear();
        userRepository.save(user);

    }
    public int calculateSumPrice(Order order) {
        int sum = 0;
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrder(order);
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
