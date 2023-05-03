package com.kursach.controller;

import com.kursach.dto.ItemQuantityDto;
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
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("/users")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderService orderService;

    // Create order from user's devices
    @PostMapping("/{username}/orders")
    public ResponseEntity<HttpStatus> createOrder(@PathVariable String username,
                                              @RequestBody List<ItemQuantityDto> items,
                                              Principal principal) {
        // Call the order service to create the order
        return new ResponseEntity<>(orderService.createOrder(username, items, principal));
    }

    @GetMapping("/{username}/orders/{orderId}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable String username,
                                                        @PathVariable Long orderId,
                                                        Principal principal) {
        return orderService.getOrderById(orderId, principal);
    }

    @GetMapping("/{username}/orders")
    public ResponseEntity<Optional<List<Order>>> getOrders(@PathVariable String username,
                                                     Principal principal){
        return orderService.getOrders(username, principal);
    }

    // Add device to user's device list
    @PostMapping("/{username}/devices")
    public ResponseEntity<HttpStatus> addDeviceToUser(@PathVariable String username,
                                      @RequestBody Device device,
                                      Principal principal) {
        return new ResponseEntity<>(userService.addDeviceToUser(username, device, principal));
    }

    @GetMapping("/{username}/devices")
    public ResponseEntity<Set<Device>> getDeviceSetFromUser(@PathVariable String username,
                                                            Principal principal) {
        return userService.getDeviceSetFromUser(username, principal);
    }

    // Delete device from user's device list
    @PostMapping ("/{username}/devices/{deviceId}")
    public ResponseEntity<HttpStatus> deleteDeviceFromUser(@PathVariable String username,
                                                           @PathVariable Long deviceId,
                                                           Principal principal) {
        return new ResponseEntity<>(userService.deleteDevice(deviceService.getById(deviceId), username, principal));
    }

}
