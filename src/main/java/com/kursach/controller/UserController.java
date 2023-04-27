package com.kursach.controller;

import com.kursach.entity.Device;
import com.kursach.entity.User;
import com.kursach.service.DeviceService;
import com.kursach.service.DeviceServiceImplementation;
import com.kursach.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    private final UserService userService;
    private final DeviceServiceImplementation deviceService;

    public UserController(UserService userService, DeviceServiceImplementation deviceService){
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}/cart")
    public Set<Device> getCart(@PathVariable("id") Long id){
        return userService.findUserById(id).getDevices();
    }

//    @PostMapping("/{id}/cart")
//    public HttpStatus addToCart(@PathVariable("id") Long id){
//        Set<Device> devices = userService.findUserById(id).getDevices();
//        return userService.findUserById(id).setDevices(devices);
//    }

}
