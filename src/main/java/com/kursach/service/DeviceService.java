package com.kursach.service;

import com.kursach.entity.Cargo;
import com.kursach.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<Device> findAll();

    Page<Device> findAll(Pageable pageable);

    Page<Device> findByPriceBetween(Integer startPrice, Integer endPrice, Pageable pageable);

    Page<Device> findByTitle(String title, Pageable pageable);

    Page<Device> findByType(String type, Pageable pageable);

    Page<Device> findByBrand(String brand, Pageable pageable);

    public HttpStatus Save(Device device);

    public HttpStatus Delete(Long deviceId);

}