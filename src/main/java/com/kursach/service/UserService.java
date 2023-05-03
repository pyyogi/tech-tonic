package com.kursach.service;

import com.kursach.dto.UserDto;
import com.kursach.entity.Device;
import com.kursach.entity.Order;
import com.kursach.entity.Role;
import com.kursach.entity.User;
import com.kursach.repository.OrderRepository;
import com.kursach.repository.RoleRepository;
import com.kursach.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    OrderRepository orderRepository;
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

    public HttpStatus addDeviceToUser(String username,
                                      Device device,
                                      Principal principal) {
        User user = userRepository.findByUsername(username);
        System.out.println(user);
        if (user == null || !user.getUsername().equals(principal.getName())) {
            return HttpStatus.NOT_FOUND;
        }

        try {
            user.getDevices().add(device);
            userRepository.save(user);
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;

    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public ResponseEntity<List<User>> allUsers() {
        HttpStatus status = HttpStatus.OK;
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, status);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

    public ResponseEntity<List<UserDto>> allUserDtos() {
        HttpStatus status = HttpStatus.OK;
        try {
            List<User> users = userRepository.findAll();
            List<UserDto> userDtos = new ArrayList<>();

            for (User user :
                    users) {
                boolean isAdmin = false;
                for (Role role:
                user.getRoles()){
                    if (role.getName().equals("ADMIN")){
                        isAdmin=true;
                    }
                }
                if (!isAdmin){
                    System.out.println(user.getRoles().toString());
                    userDtos.add(new UserDto(user.getId(),
                            user.getUsername(),
                            this.calculateUserOrders(user.getId()),
                            this.calculateUserCost(user.getId())
                    ));
                }

            }

            return new ResponseEntity<>(userDtos, status);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
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

    public boolean saveAdmin(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(2L)));
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            orderRepository.deleteByUser(user);
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }


    public ResponseEntity<Set<Device>> getDeviceSetFromUser(String username,
                                                            Principal principal) {
        HttpStatus status = HttpStatus.OK;
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getUsername().equals(principal.getName())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Set<Device> devices = user.getDevices();
            return new ResponseEntity<>(devices, status);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ;
        return new ResponseEntity<>(status);


    }

    public HttpStatus deleteDevice(Device device, String username, Principal principal) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getUsername().equals(principal.getName())) {
            return HttpStatus.NOT_FOUND;
        }

        Set<Device> devices = user.getDevices();
        if (devices == null || devices.isEmpty()) {
            return HttpStatus.OK;
        }

        if (!devices.contains(device)) {
            return HttpStatus.NOT_FOUND;
        }

        try {
            devices.remove(device);
            userRepository.save(user);
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }


    public int calculateUserOrders(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return 0;
        }
        User user = optionalUser.get();

        Optional<List<Order>> optionalOrder = orderRepository.findByUser(user);
        if (!optionalOrder.isPresent()) {
            return 0;
        }
        return optionalOrder.get().size();
    }

    public Long calculateUserCost(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return 0L;
        }
        User user = optionalUser.get();
        Optional<List<Order>> optionalOrders = orderRepository.findByUser(user);
        if (!optionalOrders.isPresent()) {
            return 0L;
        }
        Long userCost = 0L;
        List<Order> orders = optionalOrders.get();
        for (Order order :
                orders) {
            userCost += order.getSumPrice();
        }
        return userCost;

    }
}
