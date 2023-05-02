package com.kursach.service;

import com.kursach.entity.Device;
import com.kursach.entity.Role;
import com.kursach.entity.User;
import com.kursach.repository.RoleRepository;
import com.kursach.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
    public void addDeviceToDeviceList(Device device, String username) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        user.getDevices().add(device);
        userRepository.save(user);
    }
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "'USER'")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public Set<Device> getDeviceSetFromUser(String username){
        User user = userRepository.findByUsername(username);
        return user.getDevices();
    }
    public void addDeviceToUser(String username, Device device) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Set<Device> devices = user.getDevices();
        devices.add(device);

        user.setDevices(devices);
        userRepository.save(user);
    }

    public HttpStatus deleteDeviceFromDeviceList(Device device, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return HttpStatus.NOT_FOUND;
        }

        Set<Device> devices = user.getDevices();
        if (devices == null || devices.isEmpty()) {
            return HttpStatus.OK;
        }

        if (!devices.contains(device)) {
            return HttpStatus.OK;
        }

        devices.remove(device);
        userRepository.save(user);

        return HttpStatus.OK;
    }


}
