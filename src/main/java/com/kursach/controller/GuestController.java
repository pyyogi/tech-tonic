//package com.kursach.controller;
//
//import com.kursach.entity.Device;
//import com.kursach.repository.DeviceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//
//public class GuestController {
//    private final DeviceServiceImplementation deviceService;
//
//    @Autowired
//    private DeviceRepository deviceRepository;
//    public GuestController(DeviceServiceImplementation deviceService){
//        this.deviceService = deviceService;
//    }
//
//    @GetMapping("/devices")
//    public List<Device> getAll(){
//        return this.deviceService.findAll();
//    }
//
//    @GetMapping(path = "/devices/{id}")
//    public Optional<Device> getById(@PathVariable("id") Long id){
//        return this.deviceRepository.findById(id);
//    }
//}
