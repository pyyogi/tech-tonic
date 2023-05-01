package com.kursach.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @NotNull(message = "Please enter a quantity")
    @PositiveOrZero(message = "Quantity must be positive")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnoreProperties("orderItems")
    private Order order;

    public OrderItem(Device device, int quantity, Order order){
        this.device = device;
        this.quantity = quantity;
        this.order = order;
    }

    public OrderItem(){

    }

}
