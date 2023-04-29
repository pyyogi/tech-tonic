//package com.kursach.controller;
//
//import com.kursach.entity.Device;
//import com.kursach.repository.DeviceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/device")
//public class DeviceController {
//
//    @Autowired
//    private DeviceServiceImplementation deviceService;
//
//    @Autowired
//    private DeviceRepository deviceRepository;
//
//    @GetMapping(path = "/{id}")
//    @PreAuthorize("hasAuthority('USER')")
//    public Optional<Device> GetById(@PathVariable(name="id") Long id){
//        return deviceRepository.findById(id);
//    }
//
//    @PostMapping(path = "/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public HttpStatus Delete(@PathVariable(name = "id") Long id){
//        return deviceService.Delete(id);
//    }
//
//    @GetMapping
//    @PreAuthorize("hasAuthority('USER')")
//    public List<Device> GetAll(){
//        return this.deviceService.findAll();
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public HttpStatus Save(Device device){
//        return deviceService.Save(device);
//    }
//}
