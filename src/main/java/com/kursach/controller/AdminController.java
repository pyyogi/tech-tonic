package com.kursach.controller;

import com.kursach.dto.OrderDto;
import com.kursach.dto.UserDto;
import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.User;
import com.kursach.service.DeviceService;
import com.kursach.service.OrderService;
import com.kursach.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@Controller
@Slf4j
@RequestMapping("/admins")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderService orderService;
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getOrders(){
        return orderService.allOrders();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        return userService.allUserDtos();
    }

    @PostMapping("/devices")
    public ResponseEntity<HttpStatus> createDevice(@RequestBody Device device){
        return deviceService.save(device);
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id){
        if (userService.deleteUser(id)){
            return  new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<HttpStatus> deleteDevice(@PathVariable Long id){
        return deviceService.delete(id);
    }


}
