package com.kursach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
@ToString
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Please fill in this field.")
    @Length(max = 255, message = "The maximum length for this field is 255 characters.")
    private String title;

    @NotBlank(message = "Please fill in this field.")
    private int price;

    @NotBlank(message = "Please fill in this field.")
    @Length(min = 20, message = "The minimum length for this field is 20 characters.")
    private String description;

    @NotBlank(message = "Please fill in this field.")
    private String type;

    @NotBlank(message = "Please fill in this field.")
    private String brand;

    @NotBlank(message = "Please fill in this field.")
    private String filename;

    public Device(String title, int price, String description, String type, String brand, String filename) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.type = type;
        this.brand = brand;
        this.filename = filename;
    }
}
