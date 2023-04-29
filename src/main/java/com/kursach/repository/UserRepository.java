package com.kursach.repository;

import com.kursach.entity.Device;
import com.kursach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}


