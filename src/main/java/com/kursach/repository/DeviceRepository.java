package com.kursach.repository;

import com.kursach.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
//    Page<Device> findAll(Pageable pageable);
//

    Page<Device> findByPriceBetween(Integer startPrice, Integer endPrice, Pageable pageable);
    Page<Device> findByTitle(String title, Pageable pageable);
    Page<Device> findByType(String type, Pageable pageable);
    Page<Device> findByBrand(String brand, Pageable pageable);


}
