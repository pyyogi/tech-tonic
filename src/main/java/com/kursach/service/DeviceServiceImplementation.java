package com.kursach.service;

import com.kursach.entity.Cargo;
import com.kursach.entity.Device;
import com.kursach.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImplementation {
    private final DeviceRepository deviceRepository;
    @Autowired
    public DeviceServiceImplementation(DeviceRepository  deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public Page<Device> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    public Page<Device> findByPriceBetween(Integer startPrice, Integer endPrice, Pageable pageable) {
        return deviceRepository.findByPriceBetween(startPrice, endPrice, pageable);
    }

    public Page<Device> findByTitle(String title, Pageable pageable) {
        return deviceRepository.findByTitle(title, pageable);
    }

    public Page<Device> findByType(String type, Pageable pageable) {
        return deviceRepository.findByType(type, pageable);
    }

    public Page<Device> findByBrand(String brand, Pageable pageable) {
        return deviceRepository.findByBrand(brand, pageable);
    }

    public HttpStatus Save(Device device){
        deviceRepository.save(device);
        return HttpStatus.OK;
    }

    public HttpStatus Delete(Long deviceId){
        if (deviceRepository.findById(deviceId).isPresent()){
            deviceRepository.deleteById(deviceId);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
