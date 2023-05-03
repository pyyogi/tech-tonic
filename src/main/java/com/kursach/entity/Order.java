package com.kursach.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    private User user;

    @Column(name = "sum_price")
    private int sumPrice;


    public Order(User user) {
        this.user = user;
        this.orderItems = new ArrayList<>();
    }


    //    @NotBlank(message = "Пожалуйста заполните поле")
//    private String address;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "first_name")
//    private String firstName;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "last_name")
//    private String lastName;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "city")
//    private String city;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "email")
//    private String email;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "phone_number")
//    private String phoneNumber;
//    @NotBlank(message = "Пожалуйста заполните поле")
//    @Column(name = "post_index")
//    private Integer postIndex;
}
