package com.kursach.controller;

import com.kursach.entity.Cargo;
import com.kursach.repository.CargoRepository;
import com.kursach.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cargo")
public class CargoController {
    @Autowired
    private CargoService cargoService;

    private final CargoRepository cargo;

    CargoController(CargoRepository cargoRepository){
        this.cargo = cargoRepository;
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Optional<Cargo> GetById(@PathVariable(name="id") Long id){
        return this.cargo.findById(id);
    }

    @PostMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public HttpStatus Delete(@PathVariable(name = "id") Long id){
        return cargoService.Delete(id);
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('USER')")
    public List<Cargo> GetAll(){
        return this.cargo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public HttpStatus Save(Cargo cargo){
        return cargoService.Save(cargo);
    }
}
