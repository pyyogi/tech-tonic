package com.kursach.service;

import com.kursach.entity.Device;
import com.kursach.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public ResponseEntity<List<Device>> getAll() {
        HttpStatus status = HttpStatus.OK;
        try {
            List<Device> devices = deviceRepository.findAll();
            return new ResponseEntity<>(devices, status);
        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

    public Device getById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }


    public ResponseEntity<HttpStatus> save(Device device) {
        try {
            deviceRepository.save(device);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<HttpStatus> delete(Long id) {
        try {
            deviceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
